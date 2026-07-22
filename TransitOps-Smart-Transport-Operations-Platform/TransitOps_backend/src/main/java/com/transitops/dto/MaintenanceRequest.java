package com.transitops.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MaintenanceRequest {

	@NotNull(message = "Vehicle is required")
	private Long vehicleId;

	@NotBlank(message = "Type is required")
	@Size(max = 100, message = "Type must be at most 100 characters")
	private String type;

	@Size(max = 500, message = "Description must be at most 500 characters")
	private String description;
}
