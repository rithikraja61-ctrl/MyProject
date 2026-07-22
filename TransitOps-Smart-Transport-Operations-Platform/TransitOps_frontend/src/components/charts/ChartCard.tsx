import type { ReactNode } from 'react'

type ChartCardProps = {
  title: string
  subtitle?: string
  children: ReactNode
  className?: string
  delay?: number
}

export function ChartCard({ title, subtitle, children, className = '', delay = 0 }: ChartCardProps) {
  return (
    <section
      className={`app-card chart-card chart-card--animate${className ? ` ${className}` : ''}`}
      style={{ animationDelay: `${delay}ms` }}
    >
      <header className="chart-card__header">
        <h2 className="chart-card__title">{title}</h2>
        {subtitle ? <p className="chart-card__subtitle">{subtitle}</p> : null}
      </header>
      <div className="chart-card__body">{children}</div>
    </section>
  )
}
