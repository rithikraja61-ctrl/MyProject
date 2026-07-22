import { type FormEvent, useCallback, useEffect, useState } from 'react'
import { Button } from '../components/Button'
import { InputField } from '../components/InputField'
import { SelectField } from '../components/SelectField'
import { ApiError, getJson, postAuthJson } from '../lib/api'
import { notifyError, notifySuccess } from '../lib/notify'
import type { MaintenanceLog, MaintenanceRequest, MaintenanceStatus } from '../types/maintenance'
import { MAINTENANCE_STATUS_LABELS } from '../types/maintenance'
import type { Vehicle } from '../types/vehicle'

const EMPTY_FORM: MaintenanceRequest = {
  vehicleId: 0,
  type: '',
  description: '',
}

export function MaintenancePage() {
  const [logs, setLogs] = useState<MaintenanceLog[]>([])
  const [vehicles, setVehicles] = useState<Vehicle[]>([])
  const [form, setForm] = useState<MaintenanceRequest>(EMPTY_FORM)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [closingId, setClosingId] = useState<number | null>(null)
  const [listError, setListError] = useState<string | null>(null)
  const [formError, setFormError] = useState<string | null>(null)
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({})

  const loadData = useCallback(async () => {
    setLoading(true)
    setListError(null)
    try {
      const [logData, vehicleData] = await Promise.all([
        getJson<MaintenanceLog[]>('/api/maintenance'),
        getJson<Vehicle[]>('/api/vehicles'),
      ])
      setLogs(logData)
      setVehicles(vehicleData)
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to load maintenance'
      setListError(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void loadData()
  }, [loadData])

  function updateField<K extends keyof MaintenanceRequest>(key: K, value: MaintenanceRequest[K]) {
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

    if (!form.vehicleId) {
      setFormError('Select a vehicle')
      return
    }

    setSubmitting(true)
    setFieldErrors({})
    setFormError(null)

    try {
      await postAuthJson<MaintenanceLog>('/api/maintenance', {
        vehicleId: Number(form.vehicleId),
        type: form.type,
        description: form.description || undefined,
      })
      notifySuccess('Maintenance logged')
      setForm(EMPTY_FORM)
      await loadData()
    } catch (err) {
      if (err instanceof ApiError && err.errors) {
        setFieldErrors(err.errors)
        notifyError('Please fix the highlighted fields')
      } else {
        const message = err instanceof ApiError ? err.message : 'Failed to log maintenance'
        setFormError(message)
        notifyError(message)
      }
    } finally {
      setSubmitting(false)
    }
  }

  async function handleClose(id: number) {
    if (closingId !== null) {
      return
    }

    setClosingId(id)
    try {
      await postAuthJson<MaintenanceLog>(`/api/maintenance/${id}/close`, {})
      notifySuccess('Maintenance closed')
      await loadData()
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to close maintenance'
      notifyError(message)
    } finally {
      setClosingId(null)
    }
  }

  const availableVehicles = vehicles.filter((v) => v.status === 'AVAILABLE')

  const vehicleOptions = [
    { value: '', label: 'Select vehicle' },
    ...availableVehicles.map((v) => ({
      value: String(v.id),
      label: `${v.registrationNumber} — ${v.name}`,
    })),
  ]

  return (
    <>
      <header className="app-page__header">
        <h1 className="app-page__title">Maintenance</h1>
        <p className="app-page__subtitle">Log service and track vehicles in shop</p>
      </header>

      <div className="vehicles-layout">
        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Log maintenance
          </h2>
          {formError ? <p className="app-error">{formError}</p> : null}
          <form className="vehicles-form" onSubmit={handleSubmit}>
            <SelectField
              id="maint-vehicle"
              label="Vehicle"
              value={form.vehicleId ? String(form.vehicleId) : ''}
              options={vehicleOptions}
              onChange={(value) => updateField('vehicleId', Number(value) || 0)}
              error={fieldErrors.vehicleId}
              disabled={submitting}
            />
            <InputField
              id="maint-type"
              label="Type"
              value={form.type}
              onChange={(value) => updateField('type', value)}
              error={fieldErrors.type}
              disabled={submitting}
            />
            <InputField
              id="maint-description"
              label="Description"
              value={form.description}
              onChange={(value) => updateField('description', value)}
              error={fieldErrors.description}
              disabled={submitting}
            />
            <div className="vehicles-form__actions">
              <Button type="submit" variant="primary" disabled={submitting}>
                {submitting ? 'Saving…' : 'Log maintenance'}
              </Button>
            </div>
          </form>
        </section>

        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Maintenance log
          </h2>
          {loading ? <p className="app-loading">Loading maintenance…</p> : null}
          {listError ? <p className="app-error">{listError}</p> : null}
          {!loading && !listError && logs.length === 0 ? (
            <p className="app-card--empty">No maintenance records yet.</p>
          ) : null}
          {!loading && logs.length > 0 ? (
            <div className="vehicles-table-wrap">
              <table className="vehicles-table">
                <thead>
                  <tr>
                    <th>Vehicle</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {logs.map((log) => (
                    <tr key={log.id}>
                      <td>{log.vehicleRegistration}</td>
                      <td>{log.type}</td>
                      <td>{log.description ?? '—'}</td>
                      <td>
                        <span className="vehicles-table__status">
                          {MAINTENANCE_STATUS_LABELS[log.status as MaintenanceStatus]}
                        </span>
                      </td>
                      <td>
                        {log.status === 'OPEN' ? (
                          <Button
                            variant="primary"
                            className="vehicles-table__delete"
                            onClick={() => void handleClose(log.id)}
                            disabled={closingId === log.id}
                          >
                            {closingId === log.id ? '…' : 'Close'}
                          </Button>
                        ) : null}
                      </td>
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
