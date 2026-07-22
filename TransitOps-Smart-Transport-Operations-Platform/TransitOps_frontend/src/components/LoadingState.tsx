type LoadingStateProps = {
  label?: string
  variant?: 'inline' | 'grid'
  count?: number
}

export function LoadingState({ label = 'Loading…', variant = 'inline', count = 4 }: LoadingStateProps) {
  if (variant === 'grid') {
    return (
      <div className="loading-grid" aria-busy="true" aria-label={label}>
        {Array.from({ length: count }, (_, index) => (
          <div key={index} className="skeleton skeleton--kpi" style={{ animationDelay: `${index * 80}ms` }} />
        ))}
      </div>
    )
  }

  return (
    <div className="loading-inline" aria-busy="true" aria-label={label}>
      <span className="loading-inline__spinner" aria-hidden="true" />
      <span>{label}</span>
    </div>
  )
}
