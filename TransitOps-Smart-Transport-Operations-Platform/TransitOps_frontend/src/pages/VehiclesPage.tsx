import { type FormEvent, useCallback, useEffect, useState } from 'react'
import { Button } from '../components/Button'
import { InputField } from '../components/InputField'
import { LoadingState } from '../components/LoadingState'
import { ApiError, deleteAuth, getJson, postAuthJson } from '../lib/api'
import { getAuthSession } from '../lib/authStorage'
import { notifyError, notifySuccess } from '../lib/notify'
import { canWriteFleet } from '../lib/scopes'
import { useSettings } from '../context/SettingsContext'
import { WEIGHT_UNIT } from '../constants/settings'
import { VEHICLE_STATUS_LABELS } from '../types/dashboard'
import type { Vehicle, VehicleRequest } from '../types/vehicle'
import type { VehicleStatus } from '../types/dashboard'

const EMPTY_FORM: VehicleRequest = {
  registrationNumber: '',
  name: '',
  type: '',
  maxLoadCapacity: 0,
  odometer: 0,
  acquisitionCost: 0,
}

function statusClass(status: VehicleStatus): string {
  return `vehicles-table__status vehicles-table__status--${status.toLowerCase()}`
}

export function VehiclesPage() {
  const session = getAuthSession()
  const { formatters } = useSettings()
  const canWrite = session ? canWriteFleet(session.user.scopes) : false
  const [vehicles, setVehicles] = useState<Vehicle[]>([])
  const [form, setForm] = useState<VehicleRequest>(EMPTY_FORM)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [deletingId, setDeletingId] = useState<number | null>(null)
  const [listError, setListError] = useState<string | null>(null)
  const [formError, setFormError] = useState<string | null>(null)
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({})

  const loadVehicles = useCallback(async () => {
    setLoading(true)
    setListError(null)
    try {
      const data = await getJson<Vehicle[]>('/api/vehicles')
      setVehicles(data)
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to load vehicles'
      setListError(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void loadVehicles()
  }, [loadVehicles])

  function updateField<K extends keyof VehicleRequest>(key: K, value: VehicleRequest[K]) {
    setForm((prev) => ({ ...prev, [key]: value }))
    setFieldErrors((prev) => {
      if (!prev[key]) {
        return prev
      }
      const next = { ...prev }
      delete next[key]
      return next
    })
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    if (submitting) {
      return
    }

    setSubmitting(true)
    setFieldErrors({})
    setFormError(null)

    try {
      await postAuthJson<Vehicle>('/api/vehicles', {
        ...form,
        maxLoadCapacity: Number(form.maxLoadCapacity),
        odometer: formatters.displayToKm(Number(form.odometer)),
        acquisitionCost: Number(form.acquisitionCost),
      })
      notifySuccess('Vehicle registered')
      setForm(EMPTY_FORM)
      await loadVehicles()
    } catch (err) {
      if (err instanceof ApiError && err.errors) {
        setFieldErrors(err.errors)
        notifyError('Please fix the highlighted fields')
      } else {
        const message = err instanceof ApiError ? err.message : 'Failed to create vehicle'
        setFormError(message)
        notifyError(message)
      }
    } finally {
      setSubmitting(false)
    }
  }

  async function handleDelete(id: number) {
    if (deletingId !== null) {
      return
    }

    setDeletingId(id)
    try {
      await deleteAuth(`/api/vehicles/${id}`)
      notifySuccess('Vehicle removed')
      await loadVehicles()
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to delete vehicle'
      notifyError(message)
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <>
      <header className="app-page__header">
        <h1 className="app-page__title">Vehicles</h1>
        <p className="app-page__subtitle">
          {canWrite ? 'Fleet registry' : 'Fleet overview'}
          {!canWrite ? <span className="read-only-badge" style={{ marginLeft: '0.75rem' }}>View only</span> : null}
        </p>
      </header>

      <div className={`vehicles-layout${canWrite ? '' : ' vehicles-layout--single'}`}>
        {canWrite ? (
        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Register vehicle
          </h2>
          {formError ? <p className="app-error">{formError}</p> : null}
          <form className="vehicles-form" onSubmit={handleSubmit}>
            <InputField
              id="reg-number"
              label="Registration number"
              value={form.registrationNumber}
              onChange={(value) => updateField('registrationNumber', value)}
              error={fieldErrors.registrationNumber}
              disabled={submitting}
            />
            <InputField
              id="vehicle-name"
              label="Name / model"
              value={form.name}
              onChange={(value) => updateField('name', value)}
              error={fieldErrors.name}
              disabled={submitting}
            />
            <InputField
              id="vehicle-type"
              label="Type"
              value={form.type}
              onChange={(value) => updateField('type', value)}
              error={fieldErrors.type}
              disabled={submitting}
            />
            <InputField
              id="max-capacity"
              label={`Max load capacity (${WEIGHT_UNIT})`}
              value={form.maxLoadCapacity === 0 ? '' : String(form.maxLoadCapacity)}
              onChange={(value) => updateField('maxLoadCapacity', Number(value) || 0)}
              error={fieldErrors.maxLoadCapacity}
              disabled={submitting}
            />
            <InputField
              id="odometer"
              label={`Odometer (${formatters.distanceShort})`}
              value={form.odometer === 0 ? '' : String(form.odometer)}
              onChange={(value) => updateField('odometer', Number(value) || 0)}
              error={fieldErrors.odometer}
              disabled={submitting}
            />
            <InputField
              id="acquisition-cost"
              label={`Acquisition cost (${formatters.currencySymbol})`}
              value={form.acquisitionCost === 0 ? '' : String(form.acquisitionCost)}
              onChange={(value) => updateField('acquisitionCost', Number(value) || 0)}
              error={fieldErrors.acquisitionCost}
              disabled={submitting}
            />
            <div className="vehicles-form__actions">
              <Button type="submit" variant="primary" disabled={submitting}>
                {submitting ? 'Saving…' : 'Register'}
              </Button>
            </div>
          </form>
        </section>
        ) : null}

        <section className="app-card list-panel">
          <div className="list-panel__meta">
            <h2 className="app-page__title" style={{ fontSize: '1.125rem', margin: 0 }}>
              Fleet list
            </h2>
            {!loading && !listError ? (
              <p className="list-panel__count">{vehicles.length} vehicle{vehicles.length === 1 ? '' : 's'}</p>
            ) : null}
          </div>
          {loading ? <LoadingState label="Loading vehicles…" /> : null}
          {listError ? <p className="app-error">{listError}</p> : null}
          {!loading && !listError && vehicles.length === 0 ? (
            <p className="app-card--empty">No vehicles registered yet.</p>
          ) : null}
          {!loading && vehicles.length > 0 ? (
            <div className="vehicles-table-wrap">
              <table className="vehicles-table">
                <thead>
                  <tr>
                    <th>Reg #</th>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Capacity</th>
                    {!canWrite ? (
                      <>
                        <th>Odometer</th>
                        <th>Acquisition</th>
                      </>
                    ) : null}
                    <th>Status</th>
                    {canWrite ? <th /> : null}
                  </tr>
                </thead>
                <tbody>
                  {vehicles.map((vehicle) => (
                    <tr key={vehicle.id}>
                      <td>{vehicle.registrationNumber}</td>
                      <td>{vehicle.name}</td>
                      <td>{vehicle.type}</td>
                      <td>{formatters.formatWeight(vehicle.maxLoadCapacity)}</td>
                      {!canWrite ? (
                        <>
                          <td>{formatters.formatDistance(vehicle.odometer)}</td>
                          <td>{formatters.formatCurrency(vehicle.acquisitionCost)}</td>
                        </>
                      ) : null}
                      <td>
                        <span className={statusClass(vehicle.status)}>
                          {VEHICLE_STATUS_LABELS[vehicle.status]}
                        </span>
                      </td>
                      {canWrite ? (
                      <td>
                        <Button
                          variant="secondary"
                          className="vehicles-table__delete"
                          onClick={() => void handleDelete(vehicle.id)}
                          disabled={deletingId === vehicle.id}
                        >
                          {deletingId === vehicle.id ? '…' : 'Delete'}
                        </Button>
                      </td>
                      ) : null}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : null}
        </section>
      </div>
    </>
  )
}
