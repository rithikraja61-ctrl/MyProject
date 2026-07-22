export type DistanceUnit = 'km' | 'mi'

export type DistanceUnitOption = {
  value: DistanceUnit
  label: string
  short: string
}

export const DISTANCE_UNIT_OPTIONS: DistanceUnitOption[] = [
  { value: 'km', label: 'Kilometers', short: 'km' },
  { value: 'mi', label: 'Miles', short: 'mi' },
]

export const DEFAULT_DISTANCE_UNIT: DistanceUnit = 'km'

const KM_PER_MILE = 1.609344

export function kmToDisplay(km: number, unit: DistanceUnit): number {
  return unit === 'mi' ? km / KM_PER_MILE : km
}

export function displayToKm(value: number, unit: DistanceUnit): number {
  return unit === 'mi' ? value * KM_PER_MILE : value
}

export function findDistanceUnit(unit: string): DistanceUnitOption {
  return (
    DISTANCE_UNIT_OPTIONS.find((option) => option.value === unit) ??
    DISTANCE_UNIT_OPTIONS[0]
  )
}
