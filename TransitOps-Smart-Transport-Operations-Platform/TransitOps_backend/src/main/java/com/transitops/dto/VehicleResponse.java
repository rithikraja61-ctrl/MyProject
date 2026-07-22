package com.transitops.dto;

import java.math.BigDecimal;

import com.transitops.entity.VehicleStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VehicleResponse {

	Long id;
	String registrationNumber;
	String name;
	String type;
	Integer maxLoadCapacity;
	Integer odometer;
	BigDecimal acquisitionCost;
	VehicleStatus status;
}
