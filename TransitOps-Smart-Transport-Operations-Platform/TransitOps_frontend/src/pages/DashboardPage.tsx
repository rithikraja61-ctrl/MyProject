import { useCallback, useEffect, useState } from 'react'
import { Button } from '../components/Button'
import { ChartCard } from '../components/charts/ChartCard'
import { DashboardFleetChart } from '../components/charts/DashboardFleetChart'
import { DashboardTripsChart } from '../components/charts/DashboardTripsChart'
import { UtilizationGauge } from '../components/charts/UtilizationGauge'
import { KpiCard } from '../components/KpiCard'
import { LoadingState } from '../components/LoadingState'
import { SelectField } from '../components/SelectField'
import { ApiError, getJson } from '../lib/api'
import {
  KPI_LABELS,
  VEHICLE_STATUS_LABELS,
  VEHICLE_STATUS_OPTIONS,
  type DashboardKpis,
  type VehicleStatus,
} from '../types/dashboard'

const VEHICLE_TYPE_OPTIONS = [
  { value: '', label: 'All types' },
  { value: 'Van', label: 'Van' },
  { value: 'Truck', label: 'Truck' },
  { value: 'Bus', label: 'Bus' },
]

function formatKpiValue(key: keyof DashboardKpis, value: number): string {
  if (key === 'fleetUtilizationPercent') {
    return `${value.toFixed(1)}%`
  }
  return String(value)
}

function formatFilterSummary(type: string, status: VehicleStatus | ''): string | null {
  if (!type && !status) {
    return null
  }

  const parts: string[] = []
  if (type) {
    parts.push(type)
  }
  if (status) {
    parts.push(VEHICLE_STATUS_LABELS[status])
  }
  return parts.join(' · ')
}

export function DashboardPage() {
  const [kpis, setKpis] = useState<DashboardKpis | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [vehicleType, setVehicleType] = useState('')
  const [vehicleStatus, setVehicleStatus] = useState<VehicleStatus | ''>('')
  const [appliedType, setAppliedType] = useState('')
  const [appliedStatus, setAppliedStatus] = useState<VehicleStatus | ''>('')
  const [refreshKey, setRefreshKey] = useState(0)

  const loadKpis = useCallback(async (type: string, status: VehicleStatus | '') => {
    setLoading(true)
    setError(null)

    const params = new URLSearchParams()
    if (type) {
      params.set('vehicleType', type)
    }
    if (status) {
      params.set('vehicleStatus', status)
    }

    const query = params.toString()
    const path = query ? `/api/dashboard/kpis?${query}` : '/api/dashboard/kpis'

    try {
      const data = await getJson<DashboardKpis>(path)
      setKpis(data)
      setAppliedType(type)
      setAppliedStatus(status)
      setRefreshKey((prev) => prev + 1)
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to load dashboard'
      setError(message)
      setKpis(null)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void loadKpis('', '')
  }, [loadKpis])

  function handleApplyFilters() {
    void loadKpis(vehicleType, vehicleStatus)
  }

  function handleClearFilters() {
    setVehicleType('')
    setVehicleStatus('')
    void loadKpis('', '')
  }

  const filterSummary = formatFilterSummary(appliedType, appliedStatus)

  return (
    <div>
      <header className="app-page__header">
        <h1 className="app-page__title">Dashboard</h1>
        <p className="app-page__subtitle">Fleet operations at a glance</p>
        {filterSummary ? (
          <p className="dashboard-filter-summary">Showing: {filterSummary}</p>
        ) : null}
      </header>

      <div className="dashboard-filters app-card app-card--compact">
        <SelectField
          id="filter-type"
          label="Vehicle type"
          value={vehicleType}
          options={VEHICLE_TYPE_OPTIONS}
          onChange={setVehicleType}
        />
        <SelectField
          id="filter-status"
          label="Vehicle status"
          value={vehicleStatus}
          options={VEHICLE_STATUS_OPTIONS}
          onChange={(value) => setVehicleStatus(value as VehicleStatus | '')}
        />
        <div className="dashboard-filters__actions">
          <Button variant="primary" onClick={handleApplyFilters}>
            Apply
          </Button>
          <Button variant="secondary" onClick={handleClearFilters}>
            Clear
          </Button>
        </div>
      </div>

      {loading ? <LoadingState label="Loading KPIs…" variant="grid" count={7} /> : null}
      {error ? <p className="app-error">{error}</p> : null}

      {!loading && !error && kpis ? (
        <div key={refreshKey} className="dashboard-content">
          <div className="kpi-grid">
            {KPI_LABELS.map(({ key, label }, index) => (
              <KpiCard
                key={key}
                label={label}
                value={formatKpiValue(key, kpis[key])}
                index={index}
              />
            ))}
          </div>

          <div className="chart-grid">
            <ChartCard title="Fleet status" subtitle="Vehicle distribution" delay={200}>
              <DashboardFleetChart kpis={kpis} />
            </ChartCard>
            <ChartCard title="Trip activity" subtitle="Active vs pending dispatches" delay={280}>
              <DashboardTripsChart kpis={kpis} />
            </ChartCard>
            <ChartCard title="Utilization" subtitle="Vehicles currently on trip" className="chart-grid__wide" delay={360}>
              <UtilizationGauge percent={kpis.fleetUtilizationPercent} />
            </ChartCard>
          </div>
        </div>
      ) : null}
    </div>
  )
}
