package com.transitops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExpenseRequest {

	@NotNull(message = "Vehicle is required")
	private Long vehicleId;

	@NotNull(message = "Date is required")
	@PastOrPresent(message = "Date cannot be in the future")
	private LocalDate logDate;

	@NotBlank(message = "Type is required")
	@Size(max = 50, message = "Type must be at most 50 characters")
	private String type;

	@Size(max = 500, message = "Description must be at most 500 characters")
	private String description;

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
	private BigDecimal amount;
}
