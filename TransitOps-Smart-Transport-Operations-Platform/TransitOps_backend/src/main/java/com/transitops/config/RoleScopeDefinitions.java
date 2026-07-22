package com.transitops.config;

import java.util.List;
import java.util.Map;

import com.transitops.entity.RoleName;

public final class RoleScopeDefinitions {

	public static final String DASHBOARD = "data:dashboard";
	public static final String FLEET = "data:fleet";
	public static final String FLEET_READ = "data:fleet:read";
	public static final String DRIVERS = "data:drivers";
	public static final String DRIVERS_READ = "data:drivers:read";
	public static final String TRIPS = "data:trips";
	public static final String TRIPS_READ = "data:trips:read";
	public static final String MAINTENANCE = "data:maintenance";
	public static final String FUEL_EXPENSES = "data:fuel_expenses";
	public static final String ANALYTICS = "data:analytics";

	private static final Map<RoleName, List<String>> CANONICAL_SCOPES = Map.of(
		RoleName.FLEET_MANAGER, List.of(DASHBOARD, FLEET, DRIVERS_READ, MAINTENANCE, ANALYTICS),
		RoleName.DISPATCHER, List.of(DASHBOARD, FLEET_READ, TRIPS),
		RoleName.SAFETY_OFFICER, List.of(DASHBOARD, DRIVERS, TRIPS_READ),
		RoleName.FINANCIAL_ANALYST, List.of(DASHBOARD, FLEET_READ, FUEL_EXPENSES, ANALYTICS)
	);

	private RoleScopeDefinitions() {
	}

	public static List<String> scopesFor(RoleName roleName) {
		return CANONICAL_SCOPES.get(roleName);
	}

	public static Map<RoleName, List<String>> all() {
		return CANONICAL_SCOPES;
	}
}
