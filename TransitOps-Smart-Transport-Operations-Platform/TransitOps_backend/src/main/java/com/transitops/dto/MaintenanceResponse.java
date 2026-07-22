package com.transitops.dto;

import com.transitops.entity.MaintenanceStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MaintenanceResponse {

	Long id;
	Long vehicleId;
	String vehicleRegistration;
	String type;
	String description;
	MaintenanceStatus status;
}
