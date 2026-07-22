package com.transitops.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReportSummaryResponse {

	double avgFuelEfficiencyKmPerL;
	double fleetUtilizationPercent;
	long totalDistanceKm;
	BigDecimal totalOperationalCost;
	List<MonthlySpendPoint> monthlyFuelSpend;
	List<ExpenseBreakdownPoint> expenseBreakdown;

	@Value
	@Builder
	public static class MonthlySpendPoint {
		String month;
		BigDecimal amount;
	}

	@Value
	@Builder
	public static class ExpenseBreakdownPoint {
		String category;
		BigDecimal amount;
	}
}
