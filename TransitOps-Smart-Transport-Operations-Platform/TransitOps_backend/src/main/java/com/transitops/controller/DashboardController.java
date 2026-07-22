package com.transitops.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transitops.dto.DashboardKpisResponse;
import com.transitops.entity.VehicleStatus;
import com.transitops.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_data:dashboard')")
public class DashboardController {

	private final DashboardService dashboardService;

	@GetMapping("/kpis")
	public DashboardKpisResponse getKpis(
		@RequestParam(required = false) String vehicleType,
		@RequestParam(required = false) VehicleStatus vehicleStatus
	) {
		return dashboardService.getKpis(vehicleType, vehicleStatus);
	}
}
