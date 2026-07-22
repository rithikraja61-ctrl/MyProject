import { type FormEvent, useCallback, useEffect, useState } from 'react'
import { Button } from '../components/Button'
import { InputField } from '../components/InputField'
import { ApiError, deleteAuth, getJson, postAuthJson } from '../lib/api'
import { getAuthSession } from '../lib/authStorage'
import { notifyError, notifySuccess } from '../lib/notify'
import { canWriteDrivers } from '../lib/scopes'
import type { Driver, DriverRequest } from '../types/driver'

const EMPTY_FORM: DriverRequest = {
  name: '',
  licenseNumber: '',
  licenseCategory: '',
  licenseExpiryDate: '',
  contactNumber: '',
  safetyScore: 0,
}

export function DriversPage() {
  const session = getAuthSession()
  const canWrite = session ? canWriteDrivers(session.user.scopes) : false
  const [drivers, setDrivers] = useState<Driver[]>([])
  const [form, setForm] = useState<DriverRequest>(EMPTY_FORM)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [deletingId, setDeletingId] = useState<number | null>(null)
  const [listError, setListError] = useState<string | null>(null)
  const [formError, setFormError] = useState<string | null>(null)
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({})

  const loadDrivers = useCallback(async () => {
    setLoading(true)
    setListError(null)
    try {
      const data = await getJson<Driver[]>('/api/drivers')
      setDrivers(data)
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to load drivers'
      setListError(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void loadDrivers()
  }, [loadDrivers])

  function updateField<K extends keyof DriverRequest>(key: K, value: DriverRequest[K]) {
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
      await postAuthJson<Driver>('/api/drivers', {
        ...form,
        safetyScore: Number(form.safetyScore),
      })
      notifySuccess('Driver registered')
      setForm(EMPTY_FORM)
      await loadDrivers()
    } catch (err) {
      if (err instanceof ApiError && err.errors) {
        setFieldErrors(err.errors)
        notifyError('Please fix the highlighted fields')
      } else {
        const message = err instanceof ApiError ? err.message : 'Failed to create driver'
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
      await deleteAuth(`/api/drivers/${id}`)
      notifySuccess('Driver removed')
      await loadDrivers()
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to delete driver'
      notifyError(message)
    } finally {
      setDeletingId(null)
    }
  }

  return (
    <>
      <header className="app-page__header">
        <h1 className="app-page__title">Drivers</h1>
        <p className="app-page__subtitle">{canWrite ? 'Driver registry' : 'Driver overview (read-only)'}</p>
      </header>

      <div className={`vehicles-layout${canWrite ? '' : ' vehicles-layout--single'}`}>
        {canWrite ? (
        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Register driver
          </h2>
          {formError ? <p className="app-error">{formError}</p> : null}
          <form className="vehicles-form" onSubmit={handleSubmit}>
            <InputField
              id="driver-name"
              label="Name"
              value={form.name}
              onChange={(value) => updateField('name', value)}
              error={fieldErrors.name}
              disabled={submitting}
            />
            <InputField
              id="license-number"
              label="License number"
              value={form.licenseNumber}
              onChange={(value) => updateField('licenseNumber', value)}
              error={fieldErrors.licenseNumber}
              disabled={submitting}
            />
            <InputField
              id="license-category"
              label="License category"
              value={form.licenseCategory}
              onChange={(value) => updateField('licenseCategory', value)}
              error={fieldErrors.licenseCategory}
              disabled={submitting}
            />
            <InputField
              id="license-expiry"
              label="License expiry date"
              type="date"
              value={form.licenseExpiryDate}
              onChange={(value) => updateField('licenseExpiryDate', value)}
              error={fieldErrors.licenseExpiryDate}
              disabled={submitting}
            />
            <InputField
              id="contact-number"
              label="Contact number"
              value={form.contactNumber}
              onChange={(value) => updateField('contactNumber', value)}
              error={fieldErrors.contactNumber}
              disabled={submitting}
            />
            <InputField
              id="safety-score"
              label="Safety score"
              value={form.safetyScore === 0 ? '' : String(form.safetyScore)}
              onChange={(value) => updateField('safetyScore', Number(value) || 0)}
              error={fieldErrors.safetyScore}
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

        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Driver list
          </h2>
          {loading ? <p className="app-loading">Loading drivers…</p> : null}
          {listError ? <p className="app-error">{listError}</p> : null}
          {!loading && !listError && drivers.length === 0 ? (
            <p className="app-card--empty">No drivers registered yet.</p>
          ) : null}
          {!loading && drivers.length > 0 ? (
            <div className="vehicles-table-wrap">
              <table className="vehicles-table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>License</th>
                    <th>Category</th>
                    <th>Expiry</th>
                    <th>Score</th>
                    <th>Status</th>
                    {canWrite ? <th /> : null}
                  </tr>
                </thead>
                <tbody>
                  {drivers.map((driver) => (
                    <tr key={driver.id}>
                      <td>{driver.name}</td>
                      <td>{driver.licenseNumber}</td>
                      <td>{driver.licenseCategory}</td>
                      <td>{driver.licenseExpiryDate}</td>
                      <td>{driver.safetyScore}</td>
                      <td>
                        <span className="vehicles-table__status">{driver.status}</span>
                      </td>
                      {canWrite ? (
                      <td>
                        <Button
                          variant="secondary"
                          className="vehicles-table__delete"
                          onClick={() => void handleDelete(driver.id)}
                          disabled={deletingId === driver.id}
                        >
                          {deletingId === driver.id ? '…' : 'Delete'}
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
