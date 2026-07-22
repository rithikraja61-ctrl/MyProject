package com.transitops.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.FuelRequest;
import com.transitops.dto.FuelResponse;
import com.transitops.dto.FuelVehicleOptionResponse;
import com.transitops.entity.FuelLog;
import com.transitops.entity.Vehicle;
import com.transitops.entity.VehicleStatus;
import com.transitops.exception.BusinessRuleException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.FuelLogRepository;
import com.transitops.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FuelService {

	private final FuelLogRepository fuelLogRepository;
	private final VehicleRepository vehicleRepository;

	@Transactional(readOnly = true)
	public List<FuelVehicleOptionResponse> listVehicleOptions() {
		return vehicleRepository.findByStatusNot(VehicleStatus.RETIRED).stream()
			.map(v -> FuelVehicleOptionResponse.builder()
				.id(v.getId())
				.registrationNumber(v.getRegistrationNumber())
				.name(v.getName())
				.build())
			.toList();
	}

	@Transactional
	public FuelResponse create(FuelRequest request) {
		Vehicle vehicle = getActiveVehicle(request.getVehicleId());

		FuelLog log = new FuelLog();
		log.setVehicle(vehicle);
		log.setLogDate(request.getLogDate());
		log.setLiters(request.getLiters());
		log.setCost(request.getCost());

		return toResponse(fuelLogRepository.save(log));
	}

	@Transactional(readOnly = true)
	public List<FuelResponse> findAll() {
		return fuelLogRepository.findAllByOrderByLogDateDescIdDesc().stream()
			.map(this::toResponse)
			.toList();
	}

	private Vehicle getActiveVehicle(Long id) {
		Vehicle vehicle = vehicleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
		if (vehicle.getStatus() == VehicleStatus.RETIRED) {
			throw new BusinessRuleException("vehicleId", "Cannot log fuel for a retired vehicle");
		}
		return vehicle;
	}

	private FuelResponse toResponse(FuelLog log) {
		Vehicle vehicle = log.getVehicle();
		return FuelResponse.builder()
			.id(log.getId())
			.vehicleId(vehicle.getId())
			.vehicleRegistration(vehicle.getRegistrationNumber())
			.logDate(log.getLogDate())
			.liters(log.getLiters())
			.cost(log.getCost())
			.build();
	}
}
