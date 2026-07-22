package com.transitops.dto;

import java.math.BigDecimal;

import com.transitops.entity.VehicleStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleRequest {

	@NotBlank(message = "Registration number is required")
	@Size(max = 50, message = "Registration number must be at most 50 characters")
	private String registrationNumber;

	@NotBlank(message = "Name is required")
	@Size(max = 100, message = "Name must be at most 100 characters")
	private String name;

	@NotBlank(message = "Type is required")
	@Size(max = 50, message = "Type must be at most 50 characters")
	private String type;

	@NotNull(message = "Max load capacity is required")
	@Min(value = 1, message = "Max load capacity must be at least 1")
	private Integer maxLoadCapacity;

	@NotNull(message = "Odometer is required")
	@Min(value = 0, message = "Odometer must be zero or greater")
	private Integer odometer;

	@NotNull(message = "Acquisition cost is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Acquisition cost must be greater than zero")
	private BigDecimal acquisitionCost;

	private VehicleStatus status;
}
