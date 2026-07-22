import type { VehicleStatus } from './dashboard'

export type Vehicle = {
  id: number
  registrationNumber: string
  name: string
  type: string
  maxLoadCapacity: number
  odometer: number
  acquisitionCost: number
  status: VehicleStatus
}

export type VehicleRequest = {
  registrationNumber: string
  name: string
  type: string
  maxLoadCapacity: number
  odometer: number
  acquisitionCost: number
  status?: VehicleStatus
}
