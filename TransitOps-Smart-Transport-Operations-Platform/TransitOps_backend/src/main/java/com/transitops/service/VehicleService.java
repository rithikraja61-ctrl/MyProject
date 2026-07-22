package com.transitops.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.VehicleRequest;
import com.transitops.dto.VehicleResponse;
import com.transitops.entity.Vehicle;
import com.transitops.entity.VehicleStatus;
import com.transitops.exception.DuplicateResourceException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository vehicleRepository;

	@Transactional
	public VehicleResponse create(VehicleRequest request) {
		String registrationNumber = request.getRegistrationNumber().trim();
		if (vehicleRepository.existsByRegistrationNumber(registrationNumber)) {
			throw new DuplicateResourceException("registrationNumber", "Registration number already exists");
		}

		Vehicle vehicle = new Vehicle();
		applyRequest(vehicle, request, registrationNumber);
		if (request.getStatus() != null) {
			vehicle.setStatus(request.getStatus());
		}

		return toResponse(vehicleRepository.save(vehicle));
	}

	@Transactional(readOnly = true)
	public List<VehicleResponse> findAll() {
		return vehicleRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public VehicleResponse findById(Long id) {
		return toResponse(getVehicle(id));
	}

	@Transactional
	public VehicleResponse update(Long id, VehicleRequest request) {
		Vehicle vehicle = getVehicle(id);
		String registrationNumber = request.getRegistrationNumber().trim();
		if (vehicleRepository.existsByRegistrationNumberAndIdNot(registrationNumber, id)) {
			throw new DuplicateResourceException("registrationNumber", "Registration number already exists");
		}

		applyRequest(vehicle, request, registrationNumber);
		if (request.getStatus() != null) {
			vehicle.setStatus(request.getStatus());
		}

		return toResponse(vehicleRepository.save(vehicle));
	}

	@Transactional
	public void delete(Long id) {
		if (!vehicleRepository.existsById(id)) {
			throw new ResourceNotFoundException("Vehicle not found: " + id);
		}
		vehicleRepository.deleteById(id);
	}

	private Vehicle getVehicle(Long id) {
		return vehicleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
	}

	private void applyRequest(Vehicle vehicle, VehicleRequest request, String registrationNumber) {
		vehicle.setRegistrationNumber(registrationNumber);
		vehicle.setName(request.getName().trim());
		vehicle.setType(request.getType().trim());
		vehicle.setMaxLoadCapacity(request.getMaxLoadCapacity());
		vehicle.setOdometer(request.getOdometer());
		vehicle.setAcquisitionCost(request.getAcquisitionCost());
	}

	private VehicleResponse toResponse(Vehicle vehicle) {
		return VehicleResponse.builder()
			.id(vehicle.getId())
			.registrationNumber(vehicle.getRegistrationNumber())
			.name(vehicle.getName())
			.type(vehicle.getType())
			.maxLoadCapacity(vehicle.getMaxLoadCapacity())
			.odometer(vehicle.getOdometer())
			.acquisitionCost(vehicle.getAcquisitionCost())
			.status(vehicle.getStatus())
			.build();
	}
}
