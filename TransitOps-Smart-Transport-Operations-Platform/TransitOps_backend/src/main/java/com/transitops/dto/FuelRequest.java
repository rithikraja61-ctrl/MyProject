package com.transitops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class FuelRequest {

	@NotNull(message = "Vehicle is required")
	private Long vehicleId;

	@NotNull(message = "Date is required")
	@PastOrPresent(message = "Date cannot be in the future")
	private LocalDate logDate;

	@NotNull(message = "Liters is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Liters must be greater than zero")
	private BigDecimal liters;

	@NotNull(message = "Cost is required")
	@DecimalMin(value = "0.0", inclusive = true, message = "Cost must be zero or greater")
	private BigDecimal cost;
}
