export type MaintenanceStatus = 'OPEN' | 'CLOSED'

export type MaintenanceLog = {
  id: number
  vehicleId: number
  vehicleRegistration: string
  type: string
  description: string | null
  status: MaintenanceStatus
}

export type MaintenanceRequest = {
  vehicleId: number
  type: string
  description: string
}

export const MAINTENANCE_STATUS_LABELS: Record<MaintenanceStatus, string> = {
  OPEN: 'Open',
  CLOSED: 'Closed',
}
