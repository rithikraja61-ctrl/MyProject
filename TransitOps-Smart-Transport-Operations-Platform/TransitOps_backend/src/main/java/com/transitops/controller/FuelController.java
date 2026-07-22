package com.transitops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.transitops.dto.FuelRequest;
import com.transitops.dto.FuelResponse;
import com.transitops.dto.FuelVehicleOptionResponse;
import com.transitops.service.FuelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fuel")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_data:fuel_expenses')")
public class FuelController {

	private final FuelService fuelService;

	@GetMapping("/vehicle-options")
	public List<FuelVehicleOptionResponse> vehicleOptions() {
		return fuelService.listVehicleOptions();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FuelResponse create(@Valid @RequestBody FuelRequest request) {
		return fuelService.create(request);
	}

	@GetMapping
	public List<FuelResponse> findAll() {
		return fuelService.findAll();
	}
}
