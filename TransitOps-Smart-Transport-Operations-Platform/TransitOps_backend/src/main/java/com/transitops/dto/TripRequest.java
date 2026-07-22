package com.transitops.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TripRequest {

	@NotBlank(message = "Source is required")
	@Size(max = 200, message = "Source must be at most 200 characters")
	private String source;

	@NotBlank(message = "Destination is required")
	@Size(max = 200, message = "Destination must be at most 200 characters")
	private String destination;

	@NotNull(message = "Vehicle is required")
	private Long vehicleId;

	@NotNull(message = "Driver is required")
	private Long driverId;

	@NotNull(message = "Cargo weight is required")
	@Min(value = 1, message = "Cargo weight must be at least 1")
	private Integer cargoWeight;

	@NotNull(message = "Planned distance is required")
	@Min(value = 1, message = "Planned distance must be at least 1")
	private Integer plannedDistance;
}
