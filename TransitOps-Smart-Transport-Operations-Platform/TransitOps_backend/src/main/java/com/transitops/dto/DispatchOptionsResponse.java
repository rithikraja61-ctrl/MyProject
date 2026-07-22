package com.transitops.dto;

import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DispatchOptionsResponse {

	List<VehicleOption> vehicles;
	List<DriverOption> drivers;

	@Value
	@Builder
	public static class VehicleOption {
		Long id;
		String registrationNumber;
		String name;
		Integer maxLoadCapacity;
	}

	@Value
	@Builder
	public static class DriverOption {
		Long id;
		String name;
		String licenseNumber;
	}
}
