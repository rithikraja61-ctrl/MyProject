export const SCOPES = {
  dashboard: 'data:dashboard',
  fleet: 'data:fleet',
  fleetRead: 'data:fleet:read',
  drivers: 'data:drivers',
  driversRead: 'data:drivers:read',
  trips: 'data:trips',
  tripsRead: 'data:trips:read',
  maintenance: 'data:maintenance',
  fuelExpenses: 'data:fuel_expenses',
  analytics: 'data:analytics',
} as const

export function hasAnyScope(userScopes: string[], required: string[]): boolean {
  return required.some((scope) => userScopes.includes(scope))
}

export function canWriteFleet(userScopes: string[]): boolean {
  return userScopes.includes(SCOPES.fleet)
}

export function canWriteDrivers(userScopes: string[]): boolean {
  return userScopes.includes(SCOPES.drivers)
}

export function canWriteTrips(userScopes: string[]): boolean {
  return userScopes.includes(SCOPES.trips)
}
