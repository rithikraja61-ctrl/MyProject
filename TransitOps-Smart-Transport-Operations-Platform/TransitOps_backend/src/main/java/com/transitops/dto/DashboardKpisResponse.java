package com.transitops.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DashboardKpisResponse {

	long activeVehicles;
	long availableVehicles;
	long vehiclesInMaintenance;
	long onTripVehicles;
	long retiredVehicles;
	long activeTrips;
	long pendingTrips;
	long driversOnDuty;
	double fleetUtilizationPercent;
}
