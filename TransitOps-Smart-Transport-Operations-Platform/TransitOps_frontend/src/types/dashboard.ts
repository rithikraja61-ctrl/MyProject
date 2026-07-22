export type DashboardKpis = {
  activeVehicles: number
  availableVehicles: number
  vehiclesInMaintenance: number
  onTripVehicles: number
  retiredVehicles: number
  activeTrips: number
  pendingTrips: number
  driversOnDuty: number
  fleetUtilizationPercent: number
}

export type VehicleStatus = 'AVAILABLE' | 'ON_TRIP' | 'IN_SHOP' | 'RETIRED'

export const VEHICLE_STATUS_OPTIONS: { value: VehicleStatus | ''; label: string }[] = [
  { value: '', label: 'All statuses' },
  { value: 'AVAILABLE', label: 'Available' },
  { value: 'ON_TRIP', label: 'On Trip' },
  { value: 'IN_SHOP', label: 'In Shop' },
  { value: 'RETIRED', label: 'Retired' },
]

export const VEHICLE_STATUS_LABELS: Record<VehicleStatus, string> = {
  AVAILABLE: 'Available',
  ON_TRIP: 'On trip',
  IN_SHOP: 'In shop',
  RETIRED: 'Retired',
}

export const KPI_LABELS: { key: keyof DashboardKpis; label: string }[] = [
  { key: 'activeVehicles', label: 'Active Vehicles' },
  { key: 'availableVehicles', label: 'Available Vehicles' },
  { key: 'vehiclesInMaintenance', label: 'In Maintenance' },
  { key: 'activeTrips', label: 'Active Trips' },
  { key: 'pendingTrips', label: 'Pending Trips' },
  { key: 'driversOnDuty', label: 'Drivers On Duty' },
  { key: 'fleetUtilizationPercent', label: 'Fleet Utilization' },
]
