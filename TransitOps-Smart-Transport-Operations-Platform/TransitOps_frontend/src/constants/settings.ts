import { DEFAULT_CURRENCY_CODE } from './currencies'
import { DEFAULT_DISTANCE_UNIT, type DistanceUnit } from './distanceUnits'

export const SETTINGS_STORAGE_KEY = 'transitops_settings'
export const FUEL_VOLUME_UNIT = 'L'
export const WEIGHT_UNIT = 'kg'

export const DEFAULT_DEPOT_NAME = 'Gandhinagar Depot GJ4'

export type AppSettings = {
  depotName: string
  currencyCode: string
  distanceUnit: DistanceUnit
}

export const DEFAULT_SETTINGS: AppSettings = {
  depotName: DEFAULT_DEPOT_NAME,
  currencyCode: DEFAULT_CURRENCY_CODE,
  distanceUnit: DEFAULT_DISTANCE_UNIT,
}

export function loadSettings(): AppSettings {
  try {
    const raw = localStorage.getItem(SETTINGS_STORAGE_KEY)
    if (!raw) {
      const legacyDepot = localStorage.getItem('transitops_depot_name')
      if (legacyDepot?.trim()) {
        return { ...DEFAULT_SETTINGS, depotName: legacyDepot.trim() }
      }
      return { ...DEFAULT_SETTINGS }
    }

    const parsed = JSON.parse(raw) as Partial<AppSettings>
    return {
      depotName: parsed.depotName?.trim() || DEFAULT_SETTINGS.depotName,
      currencyCode: parsed.currencyCode || DEFAULT_SETTINGS.currencyCode,
      distanceUnit: parsed.distanceUnit === 'mi' ? 'mi' : 'km',
    }
  } catch {
    return { ...DEFAULT_SETTINGS }
  }
}

export function saveSettings(settings: AppSettings): void {
  localStorage.setItem(
    SETTINGS_STORAGE_KEY,
    JSON.stringify({
      depotName: settings.depotName.trim(),
      currencyCode: settings.currencyCode,
      distanceUnit: settings.distanceUnit,
    }),
  )
}
