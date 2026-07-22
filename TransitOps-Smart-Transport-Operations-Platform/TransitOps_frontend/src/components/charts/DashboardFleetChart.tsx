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
import type { DashboardKpis } from '../../types/dashboard'
import {
  axisTickStyle,
  CHART_ANIMATION,
  CHART_COLORS,
  chartMargin,
  chartTooltipStyle,
} from '../../lib/chartTheme'

const STATUS_COLORS = [
  CHART_COLORS.success,
  CHART_COLORS.primary,
  CHART_COLORS.info,
  CHART_COLORS.muted,
]

type DashboardFleetChartProps = {
  kpis: DashboardKpis
}

export function DashboardFleetChart({ kpis }: DashboardFleetChartProps) {
  const data = [
    { name: 'Available', value: kpis.availableVehicles },
    { name: 'On trip', value: kpis.onTripVehicles },
    { name: 'Maintenance', value: kpis.vehiclesInMaintenance },
    { name: 'Retired', value: kpis.retiredVehicles },
  ]

  return (
    <ResponsiveContainer width="100%" height={240}>
      <BarChart data={data} margin={chartMargin}>
        <defs>
          {STATUS_COLORS.map((color, index) => (
            <linearGradient key={color} id={`fleetGrad${index}`} x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor={color} stopOpacity={0.95} />
              <stop offset="100%" stopColor={color} stopOpacity={0.55} />
            </linearGradient>
          ))}
        </defs>
        <CartesianGrid stroke={CHART_COLORS.grid} strokeDasharray="4 4" vertical={false} />
        <XAxis dataKey="name" tick={axisTickStyle} axisLine={false} tickLine={false} />
        <YAxis allowDecimals={false} tick={axisTickStyle} axisLine={false} tickLine={false} width={32} />
        <Tooltip {...chartTooltipStyle} />
        <Bar
          dataKey="value"
          radius={[6, 6, 0, 0]}
          animationDuration={CHART_ANIMATION.duration}
          animationEasing={CHART_ANIMATION.easing}
        >
          {data.map((entry, index) => (
            <Cell key={entry.name} fill={`url(#fleetGrad${index})`} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  )
}
