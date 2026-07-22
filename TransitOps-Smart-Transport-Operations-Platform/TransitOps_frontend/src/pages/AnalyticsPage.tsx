import { useCallback, useEffect, useMemo, useState } from 'react'
import {
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import { Button } from '../components/Button'
import { ChartCard } from '../components/charts/ChartCard'
import { KpiCard } from '../components/KpiCard'
import { LoadingState } from '../components/LoadingState'
import { useSettings } from '../context/SettingsContext'
import {
  axisTickStyle,
  CHART_ANIMATION,
  CHART_COLORS,
  chartMargin,
  chartTooltipStyle,
} from '../lib/chartTheme'
import { ApiError, downloadAuthFile, getJson } from '../lib/api'
import { notifyError } from '../lib/notify'
import {
  getAnalyticsKpiLabels,
  type AnalyticsKpiKey,
  type ReportSummary,
} from '../types/reports'

const EXPENSE_COLORS = [CHART_COLORS.primary, CHART_COLORS.info, CHART_COLORS.success, CHART_COLORS.warning]

export function AnalyticsPage() {
  const { formatters } = useSettings()
  const [summary, setSummary] = useState<ReportSummary | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [exporting, setExporting] = useState(false)
  const [refreshKey, setRefreshKey] = useState(0)

  const kpiLabels = useMemo(() => getAnalyticsKpiLabels(formatters), [formatters])

  function formatKpiValue(key: AnalyticsKpiKey, value: number): string {
    if (key === 'fleetUtilizationPercent') {
      return formatters.formatPercent(value)
    }
    if (key === 'avgFuelEfficiencyKmPerL') {
      return formatters.formatEfficiency(value)
    }
    if (key === 'totalOperationalCost') {
      return formatters.formatCurrency(value)
    }
    if (key === 'totalDistanceKm') {
      return formatters.formatDistance(value)
    }
    return String(value)
  }

  const loadSummary = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getJson<ReportSummary>('/api/reports/summary')
      setSummary(data)
      setRefreshKey((prev) => prev + 1)
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to load analytics'
      setError(message)
      setSummary(null)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void loadSummary()
  }, [loadSummary])

  async function handleExportCsv() {
    if (exporting) {
      return
    }

    setExporting(true)
    try {
      await downloadAuthFile('/api/reports/export.csv', 'transitops-report.csv')
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to export CSV'
      notifyError(message)
    } finally {
      setExporting(false)
    }
  }

  return (
    <div>
      <header className="app-page__header">
        <div className="app-page__header-row">
          <div>
            <h1 className="app-page__title">Analytics</h1>
            <p className="app-page__subtitle">Operational insights and cost trends</p>
          </div>
          <Button variant="primary" onClick={() => void handleExportCsv()} disabled={exporting || loading || !summary}>
            {exporting ? 'Exporting…' : 'Export CSV'}
          </Button>
        </div>
      </header>

      {loading ? <LoadingState label="Loading analytics…" variant="grid" count={4} /> : null}
      {error ? <p className="app-error">{error}</p> : null}

      {!loading && !error && summary ? (
        <div key={refreshKey} className="analytics-layout">
          <div className="kpi-grid kpi-grid--analytics">
            {kpiLabels.map(({ key, label }, index) => (
              <KpiCard
                key={key}
                label={label}
                value={formatKpiValue(key, summary[key])}
                index={index}
              />
            ))}
          </div>

          <ChartCard title="Monthly fuel spend" subtitle="Trend by month" delay={200}>
            {summary.monthlyFuelSpend.length === 0 ? (
              <p className="app-card--empty">No fuel data yet.</p>
            ) : (
              <ResponsiveContainer width="100%" height={280}>
                <BarChart data={summary.monthlyFuelSpend} margin={chartMargin}>
                  <defs>
                    <linearGradient id="fuelSpendGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="0%" stopColor={CHART_COLORS.primary} stopOpacity={0.95} />
                      <stop offset="100%" stopColor={CHART_COLORS.primary} stopOpacity={0.35} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid stroke={CHART_COLORS.grid} strokeDasharray="4 4" vertical={false} />
                  <XAxis dataKey="month" tick={axisTickStyle} axisLine={false} tickLine={false} />
                  <YAxis tick={axisTickStyle} axisLine={false} tickLine={false} width={48} />
                  <Tooltip formatter={(value) => formatters.formatCurrency(Number(value))} {...chartTooltipStyle} />
                  <Bar
                    dataKey="amount"
                    fill="url(#fuelSpendGrad)"
                    radius={[6, 6, 0, 0]}
                    animationDuration={CHART_ANIMATION.duration}
                    animationEasing={CHART_ANIMATION.easing}
                  />
                </BarChart>
              </ResponsiveContainer>
            )}
          </ChartCard>

          <ChartCard title="Expense breakdown" subtitle="By category" delay={300}>
            {summary.expenseBreakdown.length === 0 ? (
              <p className="app-card--empty">No expense data yet.</p>
            ) : (
              <ResponsiveContainer width="100%" height={280}>
                <BarChart data={summary.expenseBreakdown} layout="vertical" margin={{ top: 8, right: 16, left: 8, bottom: 8 }}>
                  <CartesianGrid stroke={CHART_COLORS.grid} strokeDasharray="4 4" horizontal={false} />
                  <XAxis type="number" tick={axisTickStyle} axisLine={false} tickLine={false} />
                  <YAxis type="category" dataKey="category" width={110} tick={axisTickStyle} axisLine={false} tickLine={false} />
                  <Tooltip formatter={(value) => formatters.formatCurrency(Number(value))} {...chartTooltipStyle} />
                  <Bar
                    dataKey="amount"
                    radius={[0, 6, 6, 0]}
                    animationDuration={CHART_ANIMATION.duration}
                    animationEasing={CHART_ANIMATION.easing}
                  >
                    {summary.expenseBreakdown.map((entry, index) => (
                      <Cell key={entry.category} fill={EXPENSE_COLORS[index % EXPENSE_COLORS.length]} />
                    ))}
                  </Bar>
                </BarChart>
              </ResponsiveContainer>
            )}
          </ChartCard>
        </div>
      ) : null}
    </div>
  )
}
