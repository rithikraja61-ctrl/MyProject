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

import com.transitops.dto.ExpenseRequest;
import com.transitops.dto.ExpenseResponse;
import com.transitops.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_data:fuel_expenses')")
public class ExpenseController {

	private final ExpenseService expenseService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ExpenseResponse create(@Valid @RequestBody ExpenseRequest request) {
		return expenseService.create(request);
	}

	@GetMapping
	public List<ExpenseResponse> findAll() {
		return expenseService.findAll();
	}
}
