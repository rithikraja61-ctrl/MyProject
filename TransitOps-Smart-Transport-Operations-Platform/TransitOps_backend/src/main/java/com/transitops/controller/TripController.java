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

import com.transitops.config.RoleScopeDefinitions;
import com.transitops.dto.DispatchOptionsResponse;
import com.transitops.dto.TripRequest;
import com.transitops.dto.TripResponse;
import com.transitops.service.TripService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

	private final TripService tripService;

	@GetMapping("/dispatch-options")
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.TRIPS + "')")
	public DispatchOptionsResponse dispatchOptions() {
		return tripService.listDispatchOptions();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.TRIPS + "')")
	public TripResponse create(@Valid @RequestBody TripRequest request) {
		return tripService.create(request);
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('SCOPE_" + RoleScopeDefinitions.TRIPS + "', 'SCOPE_" + RoleScopeDefinitions.TRIPS_READ + "')")
	public List<TripResponse> findAll() {
		return tripService.findAll();
	}

	@PostMapping("/{id}/dispatch")
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.TRIPS + "')")
	public TripResponse dispatch(@PathVariable Long id) {
		return tripService.dispatch(id);
	}

	@PostMapping("/{id}/complete")
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.TRIPS + "')")
	public TripResponse complete(@PathVariable Long id) {
		return tripService.complete(id);
	}

	@PostMapping("/{id}/cancel")
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.TRIPS + "')")
	public TripResponse cancel(@PathVariable Long id) {
		return tripService.cancel(id);
	}
}
