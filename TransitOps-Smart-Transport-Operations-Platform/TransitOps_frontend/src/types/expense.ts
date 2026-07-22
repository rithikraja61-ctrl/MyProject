export type Expense = {
  id: number
  vehicleId: number
  vehicleRegistration: string
  logDate: string
  type: string
  description: string | null
  amount: number
}

export type ExpenseRequest = {
  vehicleId: number
  logDate: string
  type: string
  description?: string
  amount: number
}

export const EXPENSE_TYPE_OPTIONS = [
  { value: '', label: 'Select type' },
  { value: 'Tolls', label: 'Tolls' },
  { value: 'Maintenance', label: 'Maintenance' },
  { value: 'Other', label: 'Other' },
]
