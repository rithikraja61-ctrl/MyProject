package com.transitops.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.ExpenseRequest;
import com.transitops.dto.ExpenseResponse;
import com.transitops.entity.Expense;
import com.transitops.entity.Vehicle;
import com.transitops.entity.VehicleStatus;
import com.transitops.exception.BusinessRuleException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.repository.ExpenseRepository;
import com.transitops.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final VehicleRepository vehicleRepository;

	@Transactional
	public ExpenseResponse create(ExpenseRequest request) {
		Vehicle vehicle = getActiveVehicle(request.getVehicleId());

		Expense expense = new Expense();
		expense.setVehicle(vehicle);
		expense.setLogDate(request.getLogDate());
		expense.setType(request.getType().trim());
		if (request.getDescription() != null && !request.getDescription().isBlank()) {
			expense.setDescription(request.getDescription().trim());
		}
		expense.setAmount(request.getAmount());

		return toResponse(expenseRepository.save(expense));
	}

	@Transactional(readOnly = true)
	public List<ExpenseResponse> findAll() {
		return expenseRepository.findAllByOrderByLogDateDescIdDesc().stream()
			.map(this::toResponse)
			.toList();
	}

	private Vehicle getActiveVehicle(Long id) {
		Vehicle vehicle = vehicleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
		if (vehicle.getStatus() == VehicleStatus.RETIRED) {
			throw new BusinessRuleException("vehicleId", "Cannot log expense for a retired vehicle");
		}
		return vehicle;
	}

	private ExpenseResponse toResponse(Expense expense) {
		Vehicle vehicle = expense.getVehicle();
		return ExpenseResponse.builder()
			.id(expense.getId())
			.vehicleId(vehicle.getId())
			.vehicleRegistration(vehicle.getRegistrationNumber())
			.logDate(expense.getLogDate())
			.type(expense.getType())
			.description(expense.getDescription())
			.amount(expense.getAmount())
			.build();
	}
}
