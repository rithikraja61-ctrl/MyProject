export type TripStatus = 'DRAFT' | 'DISPATCHED' | 'COMPLETED' | 'CANCELLED'

export type Trip = {
  id: number
  source: string
  destination: string
  vehicleId: number
  vehicleRegistration: string
  vehicleName: string
  driverId: number
  driverName: string
  cargoWeight: number
  plannedDistance: number
  status: TripStatus
}

export type TripRequest = {
  source: string
  destination: string
  vehicleId: number
  driverId: number
  cargoWeight: number
  plannedDistance: number
}

export type DispatchVehicleOption = {
  id: number
  registrationNumber: string
  name: string
  maxLoadCapacity: number
}

export type DispatchDriverOption = {
  id: number
  name: string
  licenseNumber: string
}

export type DispatchOptions = {
  vehicles: DispatchVehicleOption[]
  drivers: DispatchDriverOption[]
}

export const TRIP_STATUS_LABELS: Record<TripStatus, string> = {
  DRAFT: 'Draft',
  DISPATCHED: 'Dispatched',
  COMPLETED: 'Completed',
  CANCELLED: 'Cancelled',
}
