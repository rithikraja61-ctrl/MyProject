type KpiCardProps = {
  label: string
  value: string
  index?: number
}

export function KpiCard({ label, value, index = 0 }: KpiCardProps) {
  return (
    <article
      className="kpi-card kpi-card--animate"
      style={{ animationDelay: `${index * 70}ms` }}
    >
      <p className="kpi-card__label">{label}</p>
      <p className="kpi-card__value">{value}</p>
    </article>
  )
}
