package com.transitops.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.transitops.config.RoleScopeDefinitions;
import com.transitops.dto.DriverRequest;
import com.transitops.dto.DriverResponse;
import com.transitops.service.DriverService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

	private final DriverService driverService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.DRIVERS + "')")
	public DriverResponse create(@Valid @RequestBody DriverRequest request) {
		return driverService.create(request);
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('SCOPE_" + RoleScopeDefinitions.DRIVERS + "', 'SCOPE_" + RoleScopeDefinitions.DRIVERS_READ + "')")
	public List<DriverResponse> findAll() {
		return driverService.findAll();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('SCOPE_" + RoleScopeDefinitions.DRIVERS + "', 'SCOPE_" + RoleScopeDefinitions.DRIVERS_READ + "')")
	public DriverResponse findById(@PathVariable Long id) {
		return driverService.findById(id);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.DRIVERS + "')")
	public DriverResponse update(@PathVariable Long id, @Valid @RequestBody DriverRequest request) {
		return driverService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + RoleScopeDefinitions.DRIVERS + "')")
	public void delete(@PathVariable Long id) {
		driverService.delete(id);
	}
}
