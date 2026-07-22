package com.transitops.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FuelResponse {

	Long id;
	Long vehicleId;
	String vehicleRegistration;
	LocalDate logDate;
	BigDecimal liters;
	BigDecimal cost;
}
