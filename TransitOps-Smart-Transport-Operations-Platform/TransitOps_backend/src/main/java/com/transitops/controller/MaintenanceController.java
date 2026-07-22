package com.transitops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.transitops.dto.MaintenanceRequest;
import com.transitops.dto.MaintenanceResponse;
import com.transitops.service.MaintenanceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_data:maintenance')")
public class MaintenanceController {

	private final MaintenanceService maintenanceService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MaintenanceResponse create(@Valid @RequestBody MaintenanceRequest request) {
		return maintenanceService.open(request);
	}

	@GetMapping
	public List<MaintenanceResponse> findAll() {
		return maintenanceService.findAll();
	}

	@PostMapping("/{id}/close")
	public MaintenanceResponse close(@PathVariable Long id) {
		return maintenanceService.close(id);
	}
}
