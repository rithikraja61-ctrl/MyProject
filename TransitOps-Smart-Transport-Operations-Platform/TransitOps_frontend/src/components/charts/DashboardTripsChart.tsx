import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from 'recharts'
import type { DashboardKpis } from '../../types/dashboard'
import { CHART_ANIMATION, CHART_COLORS, chartTooltipStyle } from '../../lib/chartTheme'

const SLICE_COLORS = [CHART_COLORS.primary, CHART_COLORS.warning]

type DashboardTripsChartProps = {
  kpis: DashboardKpis
}

export function DashboardTripsChart({ kpis }: DashboardTripsChartProps) {
  const data = [
    { name: 'Active', value: kpis.activeTrips },
    { name: 'Pending', value: kpis.pendingTrips },
  ]

  const total = kpis.activeTrips + kpis.pendingTrips

  if (total === 0) {
    return <p className="app-card--empty">No trip activity yet.</p>
  }

  return (
    <div className="donut-chart">
      <ResponsiveContainer width="100%" height={240}>
        <PieChart>
          <Pie
            data={data}
            dataKey="value"
            nameKey="name"
            cx="50%"
            cy="50%"
            innerRadius={58}
            outerRadius={88}
            paddingAngle={3}
            animationDuration={CHART_ANIMATION.duration}
            animationEasing={CHART_ANIMATION.easing}
          >
            {data.map((entry, index) => (
              <Cell key={entry.name} fill={SLICE_COLORS[index]} stroke="transparent" />
            ))}
          </Pie>
          <Tooltip {...chartTooltipStyle} />
        </PieChart>
      </ResponsiveContainer>
      <div className="donut-chart__center" aria-hidden="true">
        <span className="donut-chart__total">{total}</span>
        <span className="donut-chart__label">Trips</span>
      </div>
      <ul className="chart-legend">
        {data.map((entry, index) => (
          <li key={entry.name} className="chart-legend__item">
            <span className="chart-legend__dot" style={{ background: SLICE_COLORS[index] }} />
            {entry.name} ({entry.value})
          </li>
        ))}
      </ul>
    </div>
  )
}
