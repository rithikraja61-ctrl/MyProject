import type { ReactNode } from 'react'
import { Navigate } from 'react-router-dom'
import { getDefaultAppPath } from '../constants/nav'
import { hasAnyScope } from '../lib/scopes'
import { getAuthSession } from '../lib/authStorage'

type ScopeRouteProps = {
  scopes: string[]
  children: ReactNode
}

export function ScopeRoute({ scopes, children }: ScopeRouteProps) {
  const session = getAuthSession()

  if (!session) {
    return null
  }

  if (!hasAnyScope(session.user.scopes, scopes)) {
    const fallback = getDefaultAppPath(session.user.scopes)
    if (fallback !== window.location.pathname) {
      return <Navigate to={fallback} replace />
    }

    return (
      <div className="app-card app-card--empty">
        No modules available for your role.
      </div>
    )
  }

  return children
}
