export type DriverStatus = 'AVAILABLE' | 'ON_TRIP' | 'OFF_DUTY' | 'SUSPENDED'

export type Driver = {
  id: number
  name: string
  licenseNumber: string
  licenseCategory: string
  licenseExpiryDate: string
  contactNumber: string
  safetyScore: number
  status: DriverStatus
}

export type DriverRequest = {
  name: string
  licenseNumber: string
  licenseCategory: string
  licenseExpiryDate: string
  contactNumber: string
  safetyScore: number
  status?: DriverStatus
}
