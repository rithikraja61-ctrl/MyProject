package com.transitops.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.DispatchOptionsResponse;
import com.transitops.dto.TripRequest;
import com.transitops.dto.TripResponse;
import com.transitops.entity.Driver;
import com.transitops.entity.DriverStatus;
import com.transitops.entity.Trip;
import com.transitops.entity.TripStatus;
import com.transitops.entity.Vehicle;
import com.transitops.entity.VehicleStatus;
import com.transitops.exception.BusinessRuleException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.DriverRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

	private final TripRepository tripRepository;
	private final VehicleRepository vehicleRepository;
	private final DriverRepository driverRepository;

	@Transactional(readOnly = true)
	public DispatchOptionsResponse listDispatchOptions() {
		LocalDate today = LocalDate.now();
		List<DispatchOptionsResponse.VehicleOption> vehicles = vehicleRepository
			.findByStatus(VehicleStatus.AVAILABLE)
			.stream()
			.map(v -> DispatchOptionsResponse.VehicleOption.builder()
				.id(v.getId())
				.registrationNumber(v.getRegistrationNumber())
				.name(v.getName())
				.maxLoadCapacity(v.getMaxLoadCapacity())
				.build())
			.toList();

		List<DispatchOptionsResponse.DriverOption> drivers = driverRepository
			.findByStatusAndLicenseExpiryDateGreaterThanEqual(DriverStatus.AVAILABLE, today)
			.stream()
			.map(d -> DispatchOptionsResponse.DriverOption.builder()
				.id(d.getId())
				.name(d.getName())
				.licenseNumber(d.getLicenseNumber())
				.build())
			.toList();

		return DispatchOptionsResponse.builder()
			.vehicles(vehicles)
			.drivers(drivers)
			.build();
	}

	@Transactional
	public TripResponse create(TripRequest request) {
		Vehicle vehicle = getVehicle(request.getVehicleId());
		Driver driver = getDriver(request.getDriverId());

		assertVehicleAssignable(vehicle);
		assertDriverAssignable(driver, false);
		assertCargoCapacity(vehicle, request.getCargoWeight());

		Trip trip = new Trip();
		trip.setSource(request.getSource().trim());
		trip.setDestination(request.getDestination().trim());
		trip.setVehicle(vehicle);
		trip.setDriver(driver);
		trip.setCargoWeight(request.getCargoWeight());
		trip.setPlannedDistance(request.getPlannedDistance());
		trip.setStatus(TripStatus.DRAFT);

		return toResponse(tripRepository.save(trip));
	}

	@Transactional(readOnly = true)
	public List<TripResponse> findAll() {
		return tripRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional
	public TripResponse dispatch(Long id) {
		Trip trip = getTrip(id);
		if (trip.getStatus() != TripStatus.DRAFT) {
			throw new BusinessRuleException("status", "Only draft trips can be dispatched");
		}

		Vehicle vehicle = trip.getVehicle();
		Driver driver = trip.getDriver();

		assertVehicleDispatchable(vehicle);
		assertDriverDispatchable(driver);
		assertCargoCapacity(vehicle, trip.getCargoWeight());

		vehicle.setStatus(VehicleStatus.ON_TRIP);
		driver.setStatus(DriverStatus.ON_TRIP);
		trip.setStatus(TripStatus.DISPATCHED);

		return toResponse(trip);
	}

	@Transactional
	public TripResponse complete(Long id) {
		Trip trip = getTrip(id);
		if (trip.getStatus() != TripStatus.DISPATCHED) {
			throw new BusinessRuleException("status", "Only dispatched trips can be completed");
		}

		trip.getVehicle().setStatus(VehicleStatus.AVAILABLE);
		trip.getDriver().setStatus(DriverStatus.AVAILABLE);
		trip.setStatus(TripStatus.COMPLETED);

		return toResponse(trip);
	}

	@Transactional
	public TripResponse cancel(Long id) {
		Trip trip = getTrip(id);
		if (trip.getStatus() != TripStatus.DRAFT && trip.getStatus() != TripStatus.DISPATCHED) {
			throw new BusinessRuleException("status", "Only draft or dispatched trips can be cancelled");
		}

		if (trip.getStatus() == TripStatus.DISPATCHED) {
			trip.getVehicle().setStatus(VehicleStatus.AVAILABLE);
			trip.getDriver().setStatus(DriverStatus.AVAILABLE);
		}

		trip.setStatus(TripStatus.CANCELLED);
		return toResponse(trip);
	}

	private Trip getTrip(Long id) {
		return tripRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + id));
	}

	private Vehicle getVehicle(Long id) {
		return vehicleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
	}

	private Driver getDriver(Long id) {
		return driverRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Driver not found: " + id));
	}

	private void assertVehicleAssignable(Vehicle vehicle) {
		if (vehicle.getStatus() == VehicleStatus.RETIRED) {
			throw new BusinessRuleException("vehicleId", "Retired vehicles cannot be assigned to trips");
		}
		if (vehicle.getStatus() == VehicleStatus.IN_SHOP) {
			throw new BusinessRuleException("vehicleId", "Vehicles in maintenance cannot be assigned to trips");
		}
		if (vehicle.getStatus() == VehicleStatus.ON_TRIP) {
			throw new BusinessRuleException("vehicleId", "Vehicle is already on a trip");
		}
	}

	private void assertVehicleDispatchable(Vehicle vehicle) {
		assertVehicleAssignable(vehicle);
		if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
			throw new BusinessRuleException("vehicleId", "Vehicle is not available for dispatch");
		}
	}

	private void assertDriverAssignable(Driver driver, boolean requireAvailable) {
		if (driver.getStatus() == DriverStatus.SUSPENDED) {
			throw new BusinessRuleException("driverId", "Suspended drivers cannot be assigned to trips");
		}
		if (requireAvailable && driver.getStatus() != DriverStatus.AVAILABLE) {
			throw new BusinessRuleException("driverId", "Driver is not available for dispatch");
		}
		if (driver.getStatus() == DriverStatus.ON_TRIP) {
			throw new BusinessRuleException("driverId", "Driver is already on a trip");
		}
	}

	private void assertDriverDispatchable(Driver driver) {
		assertDriverAssignable(driver, true);
		if (driver.getLicenseExpiryDate().isBefore(LocalDate.now())) {
			throw new BusinessRuleException("driverId", "Driver license has expired");
		}
	}

	private void assertCargoCapacity(Vehicle vehicle, int cargoWeight) {
		if (cargoWeight > vehicle.getMaxLoadCapacity()) {
			throw new BusinessRuleException(
				"cargoWeight",
				"Cargo weight exceeds vehicle max load capacity (" + vehicle.getMaxLoadCapacity() + " kg)"
			);
		}
	}

	private TripResponse toResponse(Trip trip) {
		Vehicle vehicle = trip.getVehicle();
		Driver driver = trip.getDriver();
		return TripResponse.builder()
			.id(trip.getId())
			.source(trip.getSource())
			.destination(trip.getDestination())
			.vehicleId(vehicle.getId())
			.vehicleRegistration(vehicle.getRegistrationNumber())
			.vehicleName(vehicle.getName())
			.driverId(driver.getId())
			.driverName(driver.getName())
			.cargoWeight(trip.getCargoWeight())
			.plannedDistance(trip.getPlannedDistance())
			.status(trip.getStatus())
			.build();
	}
}
