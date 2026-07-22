package com.transitops.dto;

import java.time.LocalDate;

import com.transitops.entity.DriverStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DriverResponse {

	Long id;
	String name;
	String licenseNumber;
	String licenseCategory;
	LocalDate licenseExpiryDate;
	String contactNumber;
	Integer safetyScore;
	DriverStatus status;
}
