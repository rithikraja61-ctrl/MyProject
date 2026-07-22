package com.transitops.dto;

import java.time.LocalDate;

import com.transitops.entity.DriverStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DriverRequest {

	@NotBlank(message = "Name is required")
	@Size(max = 100, message = "Name must be at most 100 characters")
	private String name;

	@NotBlank(message = "License number is required")
	@Size(max = 50, message = "License number must be at most 50 characters")
	private String licenseNumber;

	@NotBlank(message = "License category is required")
	@Size(max = 20, message = "License category must be at most 20 characters")
	private String licenseCategory;

	@NotNull(message = "License expiry date is required")
	private LocalDate licenseExpiryDate;

	@NotBlank(message = "Contact number is required")
	@Size(max = 20, message = "Contact number must be at most 20 characters")
	private String contactNumber;

	@NotNull(message = "Safety score is required")
	@Min(value = 0, message = "Safety score must be between 0 and 100")
	@Max(value = 100, message = "Safety score must be between 0 and 100")
	private Integer safetyScore;

	private DriverStatus status;
}
