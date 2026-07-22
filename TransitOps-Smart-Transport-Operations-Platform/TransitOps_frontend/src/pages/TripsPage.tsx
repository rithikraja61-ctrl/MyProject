import { type FormEvent, useCallback, useEffect, useState } from 'react'
import { Button } from '../components/Button'
import { InputField } from '../components/InputField'
import { SelectField } from '../components/SelectField'
import { ApiError, getJson, postAuthJson } from '../lib/api'
import { getAuthSession } from '../lib/authStorage'
import { notifyError, notifySuccess } from '../lib/notify'
import { canWriteTrips } from '../lib/scopes'
import { useSettings } from '../context/SettingsContext'
import { WEIGHT_UNIT } from '../constants/settings'
import type {
  DispatchOptions,
  Trip,
  TripRequest,
  TripStatus,
} from '../types/trip'
import { TRIP_STATUS_LABELS } from '../types/trip'

const EMPTY_FORM: TripRequest = {
  source: '',
  destination: '',
  vehicleId: 0,
  driverId: 0,
  cargoWeight: 0,
  plannedDistance: 0,
}

type ActionKind = 'dispatch' | 'complete' | 'cancel'

export function TripsPage() {
  const session = getAuthSession()
  const { formatters } = useSettings()
  const canWrite = session ? canWriteTrips(session.user.scopes) : false
  const [trips, setTrips] = useState<Trip[]>([])
  const [options, setOptions] = useState<DispatchOptions>({ vehicles: [], drivers: [] })
  const [form, setForm] = useState<TripRequest>(EMPTY_FORM)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [actionTripId, setActionTripId] = useState<number | null>(null)
  const [actionKind, setActionKind] = useState<ActionKind | null>(null)
  const [listError, setListError] = useState<string | null>(null)
  const [formError, setFormError] = useState<string | null>(null)
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({})

  const loadData = useCallback(async () => {
    setLoading(true)
    setListError(null)
    try {
      if (canWrite) {
        const [tripData, optionData] = await Promise.all([
          getJson<Trip[]>('/api/trips'),
          getJson<DispatchOptions>('/api/trips/dispatch-options'),
        ])
        setTrips(tripData)
        setOptions(optionData)
      } else {
        const tripData = await getJson<Trip[]>('/api/trips')
        setTrips(tripData)
      }
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to load trips'
      setListError(message)
    } finally {
      setLoading(false)
    }
  }, [canWrite])

  useEffect(() => {
    void loadData()
  }, [loadData])

  function updateField<K extends keyof TripRequest>(key: K, value: TripRequest[K]) {
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

    if (!form.vehicleId || !form.driverId) {
      setFormError('Select a vehicle and driver')
      return
    }

    setSubmitting(true)
    setFieldErrors({})
    setFormError(null)

    try {
      await postAuthJson<Trip>('/api/trips', {
        ...form,
        vehicleId: Number(form.vehicleId),
        driverId: Number(form.driverId),
        cargoWeight: Number(form.cargoWeight),
        plannedDistance: formatters.displayToKm(Number(form.plannedDistance)),
      })
      notifySuccess('Trip created')
      setForm(EMPTY_FORM)
      await loadData()
    } catch (err) {
      if (err instanceof ApiError && err.errors) {
        setFieldErrors(err.errors)
        notifyError('Please fix the highlighted fields')
      } else {
        const message = err instanceof ApiError ? err.message : 'Failed to create trip'
        setFormError(message)
        notifyError(message)
      }
    } finally {
      setSubmitting(false)
    }
  }

  async function handleAction(tripId: number, kind: ActionKind) {
    if (actionTripId !== null) {
      return
    }

    setActionTripId(tripId)
    setActionKind(kind)
    const path = `/api/trips/${tripId}/${kind}`

    try {
      await postAuthJson<Trip>(path, {})
      const labels: Record<ActionKind, string> = {
        dispatch: 'Trip dispatched',
        complete: 'Trip completed',
        cancel: 'Trip cancelled',
      }
      notifySuccess(labels[kind])
      await loadData()
    } catch (err) {
      const message = err instanceof ApiError ? err.message : `Failed to ${kind} trip`
      notifyError(message)
    } finally {
      setActionTripId(null)
      setActionKind(null)
    }
  }

  const vehicleOptions = [
    { value: '', label: 'Select vehicle' },
    ...options.vehicles.map((v) => ({
      value: String(v.id),
      label: `${v.registrationNumber} — ${v.maxLoadCapacity} kg`,
    })),
  ]

  const driverOptions = [
    { value: '', label: 'Select driver' },
    ...options.drivers.map((d) => ({
      value: String(d.id),
      label: `${d.name} (${d.licenseNumber})`,
    })),
  ]

  function renderActions(trip: Trip) {
    if (!canWrite) {
      return null
    }

    const busy = actionTripId === trip.id
    const actions: { kind: ActionKind; label: string; variant: 'primary' | 'secondary' }[] = []

    if (trip.status === 'DRAFT') {
      actions.push({ kind: 'dispatch', label: 'Dispatch', variant: 'primary' })
      actions.push({ kind: 'cancel', label: 'Cancel', variant: 'secondary' })
    } else if (trip.status === 'DISPATCHED') {
      actions.push({ kind: 'complete', label: 'Complete', variant: 'primary' })
      actions.push({ kind: 'cancel', label: 'Cancel', variant: 'secondary' })
    }

    if (actions.length === 0) {
      return null
    }

    return (
      <div className="vehicles-table__actions">
        {actions.map((action) => (
          <Button
            key={action.kind}
            variant={action.variant}
            className="vehicles-table__delete"
            onClick={() => void handleAction(trip.id, action.kind)}
            disabled={busy}
          >
            {busy && actionKind === action.kind ? '…' : action.label}
          </Button>
        ))}
      </div>
    )
  }

  return (
    <>
      <header className="app-page__header">
        <h1 className="app-page__title">Trips</h1>
        <p className="app-page__subtitle">{canWrite ? 'Create and manage dispatches' : 'View trip status'}</p>
      </header>

      <div className={`vehicles-layout${canWrite ? '' : ' vehicles-layout--single'}`}>
        {canWrite ? (
        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            New trip
          </h2>
          {formError ? <p className="app-error">{formError}</p> : null}
          <form className="vehicles-form" onSubmit={handleSubmit}>
            <InputField
              id="trip-source"
              label="Source"
              value={form.source}
              onChange={(value) => updateField('source', value)}
              error={fieldErrors.source}
              disabled={submitting}
            />
            <InputField
              id="trip-destination"
              label="Destination"
              value={form.destination}
              onChange={(value) => updateField('destination', value)}
              error={fieldErrors.destination}
              disabled={submitting}
            />
            <SelectField
              id="trip-vehicle"
              label="Vehicle"
              value={form.vehicleId ? String(form.vehicleId) : ''}
              options={vehicleOptions}
              onChange={(value) => updateField('vehicleId', Number(value) || 0)}
              error={fieldErrors.vehicleId}
              disabled={submitting}
            />
            <SelectField
              id="trip-driver"
              label="Driver"
              value={form.driverId ? String(form.driverId) : ''}
              options={driverOptions}
              onChange={(value) => updateField('driverId', Number(value) || 0)}
              error={fieldErrors.driverId}
              disabled={submitting}
            />
            <InputField
              id="trip-cargo"
              label={`Cargo weight (${WEIGHT_UNIT})`}
              value={form.cargoWeight === 0 ? '' : String(form.cargoWeight)}
              onChange={(value) => updateField('cargoWeight', Number(value) || 0)}
              error={fieldErrors.cargoWeight}
              disabled={submitting}
            />
            <InputField
              id="trip-distance"
              label={`Planned distance (${formatters.distanceShort})`}
              value={form.plannedDistance === 0 ? '' : String(form.plannedDistance)}
              onChange={(value) => updateField('plannedDistance', Number(value) || 0)}
              error={fieldErrors.plannedDistance}
              disabled={submitting}
            />
            <div className="vehicles-form__actions">
              <Button type="submit" variant="primary" disabled={submitting}>
                {submitting ? 'Saving…' : 'Create draft'}
              </Button>
            </div>
          </form>
        </section>
        ) : null}

        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Trip list
          </h2>
          {loading ? <p className="app-loading">Loading trips…</p> : null}
          {listError ? <p className="app-error">{listError}</p> : null}
          {!loading && !listError && trips.length === 0 ? (
            <p className="app-card--empty">No trips yet.</p>
          ) : null}
          {!loading && trips.length > 0 ? (
            <div className="vehicles-table-wrap">
              <table className="vehicles-table">
                <thead>
                  <tr>
                    <th>Route</th>
                    <th>Vehicle</th>
                    <th>Driver</th>
                    <th>Cargo</th>
                    <th>Status</th>
                    {canWrite ? <th /> : null}
                  </tr>
                </thead>
                <tbody>
                  {trips.map((trip) => (
                    <tr key={trip.id}>
                      <td>
                        {trip.source} → {trip.destination}
                      </td>
                      <td>{trip.vehicleRegistration}</td>
                      <td>{trip.driverName}</td>
                      <td>{trip.cargoWeight} kg</td>
                      <td>
                        <span className="vehicles-table__status">
                          {TRIP_STATUS_LABELS[trip.status as TripStatus]}
                        </span>
                      </td>
                      {canWrite ? <td>{renderActions(trip)}</td> : null}
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
