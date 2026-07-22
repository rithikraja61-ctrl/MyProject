package com.transitops.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FuelVehicleOptionResponse {

	Long id;
	String registrationNumber;
	String name;
}
