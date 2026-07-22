package com.transitops.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.DashboardKpisResponse;
import com.transitops.entity.DriverStatus;
import com.transitops.entity.TripStatus;
import com.transitops.entity.VehicleStatus;
import com.transitops.repository.DriverRepository;
import com.transitops.repository.TripRepository;
import com.transitops.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final VehicleRepository vehicleRepository;
	private final DriverRepository driverRepository;
	private final TripRepository tripRepository;

	private record VehicleScope(String type, VehicleStatus status) {

		static VehicleScope of(String vehicleType, VehicleStatus vehicleStatus) {
			String type = vehicleType != null && !vehicleType.isBlank() ? vehicleType.trim() : null;
			return new VehicleScope(type, vehicleStatus);
		}

		boolean hasType() {
			return type != null;
		}

		boolean hasStatus() {
			return status != null;
		}
	}

	@Transactional(readOnly = true)
	public DashboardKpisResponse getKpis(String vehicleType, VehicleStatus vehicleStatus) {
		VehicleScope scope = VehicleScope.of(vehicleType, vehicleStatus);

		long availableVehicles = countVehicles(scope, VehicleStatus.AVAILABLE);
		long onTripVehicles = countVehicles(scope, VehicleStatus.ON_TRIP);
		long vehiclesInMaintenance = countVehicles(scope, VehicleStatus.IN_SHOP);
		long retiredVehicles = countVehicles(scope, VehicleStatus.RETIRED);
		long activeVehicles = countActiveVehicles(scope);

		double fleetUtilizationPercent = activeVehicles == 0
			? 0.0
			: (onTripVehicles * 100.0) / activeVehicles;

		long driversOnDuty = countDriversOnDuty(scope);
		long activeTrips = countTrips(scope, TripStatus.DISPATCHED);
		long pendingTrips = countTrips(scope, TripStatus.DRAFT);

		return DashboardKpisResponse.builder()
			.activeVehicles(activeVehicles)
			.availableVehicles(availableVehicles)
			.vehiclesInMaintenance(vehiclesInMaintenance)
			.onTripVehicles(onTripVehicles)
			.retiredVehicles(retiredVehicles)
			.activeTrips(activeTrips)
			.pendingTrips(pendingTrips)
			.driversOnDuty(driversOnDuty)
			.fleetUtilizationPercent(fleetUtilizationPercent)
			.build();
	}

	private long countVehicles(VehicleScope scope, VehicleStatus targetStatus) {
		if (scope.hasStatus() && scope.status() != targetStatus) {
			return 0;
		}
		return vehicleRepository.countByStatusAndOptionalType(targetStatus, scope.type());
	}

	private long countActiveVehicles(VehicleScope scope) {
		if (scope.hasStatus()) {
			return scope.status() == VehicleStatus.RETIRED ? 0 : countVehicles(scope, scope.status());
		}
		if (scope.hasType()) {
			return vehicleRepository.countByTypeAndStatusNot(scope.type(), VehicleStatus.RETIRED);
		}
		return vehicleRepository.countByStatusNot(VehicleStatus.RETIRED);
	}

	private long countTrips(VehicleScope scope, TripStatus tripStatus) {
		return tripRepository.countByStatusAndVehicleFilters(
			tripStatus,
			scope.type(),
			scope.status()
		);
	}

	private long countDriversOnDuty(VehicleScope scope) {
		if (scope.hasStatus() && scope.status() == VehicleStatus.RETIRED) {
			return 0;
		}

		long onTripDrivers = driverRepository.countDistinctDriversOnTripsForVehicleScope(
			TripStatus.DISPATCHED,
			scope.type(),
			scope.status()
		);
		long availableDrivers = driverRepository.countByStatus(DriverStatus.AVAILABLE);
		return onTripDrivers + availableDrivers;
	}
}
