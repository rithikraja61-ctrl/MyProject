import { RadialBar, RadialBarChart, ResponsiveContainer } from 'recharts'
import { CHART_ANIMATION, CHART_COLORS } from '../../lib/chartTheme'

type UtilizationGaugeProps = {
  percent: number
  label?: string
}

export function UtilizationGauge({ percent, label = 'Fleet utilization' }: UtilizationGaugeProps) {
  const clamped = Math.min(100, Math.max(0, percent))
  const data = [{ name: label, value: clamped, fill: CHART_COLORS.primary }]

  return (
    <div className="gauge-chart">
      <ResponsiveContainer width="100%" height={200}>
        <RadialBarChart
          cx="50%"
          cy="100%"
          innerRadius="72%"
          outerRadius="100%"
          barSize={14}
          data={data}
          startAngle={180}
          endAngle={0}
        >
          <RadialBar
            dataKey="value"
            cornerRadius={8}
            background={{ fill: 'rgba(55, 65, 81, 0.6)' }}
            animationDuration={CHART_ANIMATION.duration}
            animationEasing={CHART_ANIMATION.easing}
          />
        </RadialBarChart>
      </ResponsiveContainer>
      <div className="gauge-chart__value">
        <span className="gauge-chart__percent">{clamped.toFixed(1)}%</span>
        <span className="gauge-chart__label">{label}</span>
      </div>
    </div>
  )
}
