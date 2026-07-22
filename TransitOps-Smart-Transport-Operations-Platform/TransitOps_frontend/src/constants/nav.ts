import { hasAnyScope, SCOPES } from '../lib/scopes'

export type NavItem = {
  path: string
  label: string
  scopes: string[]
}

export const APP_NAV_ITEMS: NavItem[] = [
  { path: '/', label: 'Dashboard', scopes: [SCOPES.dashboard] },
  { path: '/vehicles', label: 'Fleet', scopes: [SCOPES.fleet, SCOPES.fleetRead] },
  { path: '/drivers', label: 'Drivers', scopes: [SCOPES.drivers, SCOPES.driversRead] },
  { path: '/trips', label: 'Trips', scopes: [SCOPES.trips, SCOPES.tripsRead] },
  { path: '/maintenance', label: 'Maintenance', scopes: [SCOPES.maintenance] },
  { path: '/fuel-expenses', label: 'Fuel & Expenses', scopes: [SCOPES.fuelExpenses] },
  { path: '/analytics', label: 'Analytics', scopes: [SCOPES.analytics] },
]

export const SETTINGS_NAV = { path: '/settings', label: 'Settings' }

export function getDefaultAppPath(scopes: string[]): string {
  const first = APP_NAV_ITEMS.find((item) => hasAnyScope(scopes, item.scopes))
  return first?.path ?? '/'
}
