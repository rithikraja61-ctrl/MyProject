package com.transitops.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.MaintenanceRequest;
import com.transitops.dto.MaintenanceResponse;
import com.transitops.entity.MaintenanceLog;
import com.transitops.entity.MaintenanceStatus;
import com.transitops.entity.Vehicle;
import com.transitops.entity.VehicleStatus;
import com.transitops.exception.BusinessRuleException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.MaintenanceRepository;
import com.transitops.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

	private final MaintenanceRepository maintenanceRepository;
	private final VehicleRepository vehicleRepository;

	@Transactional
	public MaintenanceResponse open(MaintenanceRequest request) {
		Vehicle vehicle = getVehicle(request.getVehicleId());
		assertVehicleCanEnterMaintenance(vehicle);

		if (maintenanceRepository.existsByVehicleIdAndStatus(vehicle.getId(), MaintenanceStatus.OPEN)) {
			throw new BusinessRuleException("vehicleId", "Vehicle already has open maintenance");
		}

		MaintenanceLog log = new MaintenanceLog();
		log.setVehicle(vehicle);
		log.setType(request.getType().trim());
		if (request.getDescription() != null && !request.getDescription().isBlank()) {
			log.setDescription(request.getDescription().trim());
		}
		log.setStatus(MaintenanceStatus.OPEN);

		vehicle.setStatus(VehicleStatus.IN_SHOP);

		return toResponse(maintenanceRepository.save(log));
	}

	@Transactional(readOnly = true)
	public List<MaintenanceResponse> findAll() {
		return maintenanceRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Transactional
	public MaintenanceResponse close(Long id) {
		MaintenanceLog log = getLog(id);
		if (log.getStatus() != MaintenanceStatus.OPEN) {
			throw new BusinessRuleException("status", "Only open maintenance can be closed");
		}

		Vehicle vehicle = log.getVehicle();
		if (vehicle.getStatus() != VehicleStatus.RETIRED) {
			vehicle.setStatus(VehicleStatus.AVAILABLE);
		}
		log.setStatus(MaintenanceStatus.CLOSED);

		return toResponse(log);
	}

	private MaintenanceLog getLog(Long id) {
		return maintenanceRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Maintenance log not found: " + id));
	}

	private Vehicle getVehicle(Long id) {
		return vehicleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
	}

	private void assertVehicleCanEnterMaintenance(Vehicle vehicle) {
		if (vehicle.getStatus() == VehicleStatus.RETIRED) {
			throw new BusinessRuleException("vehicleId", "Retired vehicles cannot enter maintenance");
		}
		if (vehicle.getStatus() == VehicleStatus.ON_TRIP) {
			throw new BusinessRuleException("vehicleId", "Vehicle is on a trip and cannot enter maintenance");
		}
		if (vehicle.getStatus() == VehicleStatus.IN_SHOP) {
			throw new BusinessRuleException("vehicleId", "Vehicle is already in maintenance");
		}
	}

	private MaintenanceResponse toResponse(MaintenanceLog log) {
		Vehicle vehicle = log.getVehicle();
		return MaintenanceResponse.builder()
			.id(log.getId())
			.vehicleId(vehicle.getId())
			.vehicleRegistration(vehicle.getRegistrationNumber())
			.type(log.getType())
			.description(log.getDescription())
			.status(log.getStatus())
			.build();
	}
}
