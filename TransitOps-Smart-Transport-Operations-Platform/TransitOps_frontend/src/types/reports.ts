import type { MetricFormatters } from '../lib/formatMetrics'
import { FUEL_VOLUME_UNIT } from '../constants/settings'

export type ReportSummary = {
  avgFuelEfficiencyKmPerL: number
  fleetUtilizationPercent: number
  totalDistanceKm: number
  totalOperationalCost: number
  monthlyFuelSpend: { month: string; amount: number }[]
  expenseBreakdown: { category: string; amount: number }[]
}

export type AnalyticsKpiKey =
  | 'avgFuelEfficiencyKmPerL'
  | 'fleetUtilizationPercent'
  | 'totalDistanceKm'
  | 'totalOperationalCost'

export function getAnalyticsKpiLabels(
  formatters: MetricFormatters,
): { key: AnalyticsKpiKey; label: string }[] {
  return [
    {
      key: 'avgFuelEfficiencyKmPerL',
      label: `Avg Fuel Efficiency (${formatters.distanceShort}/${FUEL_VOLUME_UNIT})`,
    },
    { key: 'fleetUtilizationPercent', label: 'Fleet Utilization' },
    {
      key: 'totalDistanceKm',
      label: `Total Distance (${formatters.distanceShort})`,
    },
    {
      key: 'totalOperationalCost',
      label: `Operational Cost (${formatters.currencySymbol})`,
    },
  ]
}
