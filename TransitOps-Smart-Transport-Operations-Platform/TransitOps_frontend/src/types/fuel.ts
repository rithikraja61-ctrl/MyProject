export type EntryType = 'fuel' | 'expense'

export const ENTRY_TYPE_OPTIONS = [
  { value: 'fuel', label: 'Fuel' },
  { value: 'expense', label: 'Expense' },
]

export type FuelVehicleOption = {
  id: number
  registrationNumber: string
  name: string
}

export type FuelLog = {
  id: number
  vehicleId: number
  vehicleRegistration: string
  logDate: string
  liters: number
  cost: number
}

export type FuelRequest = {
  vehicleId: number
  logDate: string
  liters: number
  cost: number
}
