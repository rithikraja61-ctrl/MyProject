import { useEffect, useState } from 'react'
import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom'
import { Button } from '../../components/Button'
import { APP_NAV_ITEMS, SETTINGS_NAV } from '../../constants/nav'
import { getRoleLabel } from '../../constants/roles'
import { getJson } from '../../lib/api'
import { hasAnyScope } from '../../lib/scopes'
import { signOut } from '../../lib/auth'
import { getAuthSession, updateAuthSessionScopes } from '../../lib/authStorage'
import { notifySuccess } from '../../lib/notify'
import { useSettings } from '../../context/SettingsContext'
import type { AuthSession, SessionResponse } from '../../types/auth'

function userInitials(username: string): string {
  const parts = username.split(/[_\s-]+/).filter(Boolean)
  if (parts.length >= 2) {
    return `${parts[0][0]}${parts[1][0]}`.toUpperCase()
  }
  return username.slice(0, 2).toUpperCase()
}

export function AppShell() {
  const navigate = useNavigate()
  const location = useLocation()
  const [signingOut, setSigningOut] = useState(false)
  const [session, setSession] = useState<AuthSession | null>(() => getAuthSession())

  useEffect(() => {
    let cancelled = false

    async function refreshScopes() {
      try {
        const me = await getJson<SessionResponse>('/api/auth/me')
        updateAuthSessionScopes(me.scopes)
        if (!cancelled) {
          setSession(getAuthSession())
        }
      } catch {
        // Keep cached session if refresh fails.
      }
    }

    void refreshScopes()

    return () => {
      cancelled = true
    }
  }, [])

  if (!session) {
    return null
  }

  const { settings } = useSettings()
  const { user } = session

  async function handleSignOut() {
    if (signingOut) {
      return
    }

    setSigningOut(true)
    try {
      await signOut()
      notifySuccess('Signed out successfully')
      navigate('/signin', { replace: true })
    } finally {
      setSigningOut(false)
    }
  }

  return (
    <div className="app-shell">
      <aside className="app-shell__sidebar" aria-label="Module navigation">
        <h1 className="app-shell__title">TransitOps</h1>
        <nav className="app-shell__nav">
          {APP_NAV_ITEMS.map((item) => {
            const hasAccess = hasAnyScope(user.scopes, item.scopes)
            const isActive = location.pathname === item.path

            if (!hasAccess) {
              return (
                <span
                  key={item.path}
                  className="app-shell__nav-link app-shell__nav-link--disabled"
                  title="No access for your role"
                >
                  {item.label}
                </span>
              )
            }

            return (
              <Link
                key={item.path}
                to={item.path}
                className={`app-shell__nav-link${isActive ? ' app-shell__nav-link--active' : ''}`}
              >
                {item.label}
              </Link>
            )
          })}
          <Link
            to={SETTINGS_NAV.path}
            className={`app-shell__nav-link${location.pathname === SETTINGS_NAV.path ? ' app-shell__nav-link--active' : ''}`}
          >
            {SETTINGS_NAV.label}
          </Link>
        </nav>
      </aside>

      <div className="app-shell__content">
        <header className="app-shell__topbar">
          <div className="app-shell__topbar-start">
            <span className="app-shell__topbar-divider" aria-hidden="true" />
            <span className="app-shell__depot-name">{settings.depotName}</span>
          </div>
          <div className="app-shell__topbar-end">
          <div className="app-shell__user">
            <div className="app-shell__avatar" aria-hidden="true">
              {userInitials(user.username)}
            </div>
            <div className="app-shell__user-meta">
              <span className="app-shell__username">{user.username}</span>
              <span className="app-shell__role">{getRoleLabel(user.role)}</span>
            </div>
          </div>
          <Button
            variant="secondary"
            className="app-shell__signout"
            onClick={handleSignOut}
            disabled={signingOut}
          >
            {signingOut ? 'Signing out…' : 'Sign out'}
          </Button>
          </div>
        </header>

        <main className="app-shell__main">
          <div key={location.pathname} className="app-shell__page">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  )
}
