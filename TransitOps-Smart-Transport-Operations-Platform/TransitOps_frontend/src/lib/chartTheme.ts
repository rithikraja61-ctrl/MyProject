export const CHART_COLORS = {
  primary: '#f97316',
  primarySoft: '#fb923c',
  success: '#22c55e',
  info: '#38bdf8',
  warning: '#eab308',
  muted: '#6b7280',
  grid: 'rgba(55, 65, 81, 0.45)',
} as const

export const CHART_ANIMATION = {
  duration: 800,
  easing: 'ease-out' as const,
}

export const chartMargin = { top: 8, right: 8, left: 0, bottom: 0 }

export const axisTickStyle = {
  fill: '#9ca3af',
  fontSize: 12,
}

export const chartTooltipStyle = {
  contentStyle: {
    background: '#1f2937',
    border: '1px solid #374151',
    borderRadius: '8px',
    fontSize: '0.8125rem',
    color: '#f9fafb',
    boxShadow: '0 8px 24px rgba(0, 0, 0, 0.35)',
  },
  labelStyle: { color: '#9ca3af' },
  cursor: { fill: 'rgba(249, 115, 22, 0.08)' },
}
