import { findCurrency } from '../constants/currencies'
import {
  displayToKm,
  findDistanceUnit,
  kmToDisplay,
  type DistanceUnit,
} from '../constants/distanceUnits'
import { FUEL_VOLUME_UNIT, WEIGHT_UNIT, type AppSettings } from '../constants/settings'

export type MetricFormatters = {
  currencyCode: string
  currencySymbol: string
  distanceUnit: DistanceUnit
  distanceShort: string
  formatDistance: (km: number) => string
  formatCurrency: (amount: number) => string
  formatEfficiency: (kmPerL: number) => string
  formatLiters: (liters: number) => string
  formatWeight: (kg: number) => string
  formatPercent: (value: number) => string
  kmToDisplay: (km: number) => number
  displayToKm: (value: number) => number
}

export function createFormatters(settings: AppSettings): MetricFormatters {
  const currency = findCurrency(settings.currencyCode)
  const distance = findDistanceUnit(settings.distanceUnit)
  const unit = settings.distanceUnit

  return {
    currencyCode: currency.code,
    currencySymbol: currency.symbol,
    distanceUnit: unit,
    distanceShort: distance.short,
    formatDistance(km: number) {
      const value = kmToDisplay(km, unit)
      return `${value.toFixed(1)} ${distance.short}`
    },
    formatCurrency(amount: number) {
      return `${currency.symbol}${amount.toFixed(2)}`
    },
    formatEfficiency(kmPerL: number) {
      const perUnit = unit === 'mi' ? kmPerL / 1.609344 : kmPerL
      return `${perUnit.toFixed(1)} ${distance.short}/${FUEL_VOLUME_UNIT}`
    },
    formatLiters(liters: number) {
      return `${liters} ${FUEL_VOLUME_UNIT}`
    },
    formatWeight(kg: number) {
      return `${kg} ${WEIGHT_UNIT}`
    },
    formatPercent(value: number) {
      return `${value.toFixed(1)}%`
    },
    kmToDisplay(km: number) {
      return kmToDisplay(km, unit)
    },
    displayToKm(value: number) {
      return displayToKm(value, unit)
    },
  }
}
