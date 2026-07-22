package com.transitops.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.ReportSummaryResponse;
import com.transitops.dto.ReportSummaryResponse.ExpenseBreakdownPoint;
import com.transitops.dto.ReportSummaryResponse.MonthlySpendPoint;
import com.transitops.entity.Expense;
import com.transitops.entity.FuelLog;
import com.transitops.entity.Trip;
import com.transitops.entity.TripStatus;
import com.transitops.repository.ExpenseRepository;
import com.transitops.repository.FuelLogRepository;
import com.transitops.repository.TripRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final TripRepository tripRepository;
	private final FuelLogRepository fuelLogRepository;
	private final ExpenseRepository expenseRepository;
	private final DashboardService dashboardService;

	@Transactional(readOnly = true)
	public ReportSummaryResponse getSummary() {
		List<Trip> completedTrips = tripRepository.findByStatus(TripStatus.COMPLETED);
		List<FuelLog> fuelLogs = fuelLogRepository.findAllByOrderByLogDateDescIdDesc();
		List<Expense> expenses = expenseRepository.findAllByOrderByLogDateDescIdDesc();

		long totalDistanceKm = completedTrips.stream()
			.mapToLong(Trip::getPlannedDistance)
			.sum();

		BigDecimal totalLiters = BigDecimal.ZERO;
		BigDecimal fuelCost = BigDecimal.ZERO;
		for (FuelLog log : fuelLogs) {
			totalLiters = totalLiters.add(log.getLiters());
			fuelCost = fuelCost.add(log.getCost());
		}

		double avgFuelEfficiencyKmPerL = totalLiters.compareTo(BigDecimal.ZERO) == 0
			? 0.0
			: BigDecimal.valueOf(totalDistanceKm)
				.divide(totalLiters, 2, RoundingMode.HALF_UP)
				.doubleValue();

		BigDecimal expenseCost = BigDecimal.ZERO;
		for (Expense expense : expenses) {
			expenseCost = expenseCost.add(expense.getAmount());
		}

		double fleetUtilizationPercent = dashboardService.getKpis(null, null).getFleetUtilizationPercent();

		return ReportSummaryResponse.builder()
			.avgFuelEfficiencyKmPerL(avgFuelEfficiencyKmPerL)
			.fleetUtilizationPercent(fleetUtilizationPercent)
			.totalDistanceKm(totalDistanceKm)
			.totalOperationalCost(fuelCost.add(expenseCost))
			.monthlyFuelSpend(buildMonthlyFuelSpend(fuelLogs))
			.expenseBreakdown(buildExpenseBreakdown(expenses))
			.build();
	}

	@Transactional(readOnly = true)
	public String exportCsv() {
		return buildCsv(getSummary());
	}

	private String buildCsv(ReportSummaryResponse summary) {
		StringBuilder csv = new StringBuilder();

		csv.append("# units: distance=km,fuel=L,currency=INR\n");
		csv.append("metric,value\n");
		csv.append("avgFuelEfficiencyKmPerL,").append(summary.getAvgFuelEfficiencyKmPerL()).append('\n');
		csv.append("fleetUtilizationPercent,").append(summary.getFleetUtilizationPercent()).append('\n');
		csv.append("totalDistanceKm,").append(summary.getTotalDistanceKm()).append('\n');
		csv.append("totalOperationalCost,").append(summary.getTotalOperationalCost()).append('\n');
		csv.append('\n');
		csv.append("month,fuelCost\n");
		for (MonthlySpendPoint point : summary.getMonthlyFuelSpend()) {
			csv.append(point.getMonth()).append(',').append(point.getAmount()).append('\n');
		}
		csv.append('\n');
		csv.append("category,amount\n");
		for (ExpenseBreakdownPoint point : summary.getExpenseBreakdown()) {
			csv.append(point.getCategory()).append(',').append(point.getAmount()).append('\n');
		}

		return csv.toString();
	}

	private List<MonthlySpendPoint> buildMonthlyFuelSpend(List<FuelLog> fuelLogs) {
		Map<YearMonth, BigDecimal> byMonth = new LinkedHashMap<>();
		for (FuelLog log : fuelLogs) {
			YearMonth month = YearMonth.from(log.getLogDate());
			byMonth.merge(month, log.getCost(), BigDecimal::add);
		}

		return byMonth.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.skip(Math.max(0, byMonth.size() - 6))
			.map(entry -> MonthlySpendPoint.builder()
				.month(entry.getKey().toString())
				.amount(entry.getValue())
				.build())
			.toList();
	}

	private List<ExpenseBreakdownPoint> buildExpenseBreakdown(List<Expense> expenses) {
		Map<String, BigDecimal> byType = new LinkedHashMap<>();
		for (Expense expense : expenses) {
			byType.merge(expense.getType(), expense.getAmount(), BigDecimal::add);
		}

		return byType.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(entry -> ExpenseBreakdownPoint.builder()
				.category(entry.getKey())
				.amount(entry.getValue())
				.build())
			.toList();
	}
}
