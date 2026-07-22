package com.transitops.dto;

import com.transitops.entity.TripStatus;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TripResponse {

	Long id;
	String source;
	String destination;
	Long vehicleId;
	String vehicleRegistration;
	String vehicleName;
	Long driverId;
	String driverName;
	Integer cargoWeight;
	Integer plannedDistance;
	TripStatus status;
}
