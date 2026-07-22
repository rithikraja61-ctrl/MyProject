import { type FormEvent, useState } from 'react'
import { Button } from '../components/Button'
import { CurrencySelect } from '../components/CurrencySelect'
import { InputField } from '../components/InputField'
import { SelectField } from '../components/SelectField'
import { RBAC_MATRIX_ROWS, RBAC_MODULES } from '../constants/rbacMatrix'
import { DISTANCE_UNIT_OPTIONS, type DistanceUnit } from '../constants/distanceUnits'
import { useSettings } from '../context/SettingsContext'
import { notifyError, notifySuccess } from '../lib/notify'

export function SettingsPage() {
  const { settings, updateSettings } = useSettings()
  const [depotName, setDepotName] = useState(settings.depotName)
  const [currencyCode, setCurrencyCode] = useState(settings.currencyCode)
  const [distanceUnit, setDistanceUnit] = useState<DistanceUnit>(settings.distanceUnit)
  const [formError, setFormError] = useState<string | null>(null)
  const [submitting, setSubmitting] = useState(false)

  function handleSubmit(event: FormEvent) {
    event.preventDefault()
    if (submitting) {
      return
    }

    if (!depotName.trim()) {
      setFormError('Depot name is required')
      return
    }

    setSubmitting(true)
    setFormError(null)
    try {
      updateSettings({
        depotName: depotName.trim(),
        currencyCode,
        distanceUnit,
      })
      notifySuccess('Settings saved')
    } catch {
      notifyError('Failed to save settings')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <>
      <header className="app-page__header">
        <h1 className="app-page__title">Settings &amp; RBAC</h1>
        <p className="app-page__subtitle">Depot preferences and role access reference</p>
      </header>

      <div className="vehicles-layout">
        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            General
          </h2>
          {formError ? <p className="app-error">{formError}</p> : null}
          <form className="vehicles-form" onSubmit={handleSubmit}>
            <InputField
              id="settings-depot"
              label="Depot name"
              value={depotName}
              onChange={setDepotName}
              disabled={submitting}
            />
            <CurrencySelect
              id="settings-currency"
              label="Currency"
              value={currencyCode}
              onChange={setCurrencyCode}
              disabled={submitting}
            />
            <SelectField
              id="settings-distance"
              label="Distance unit"
              value={distanceUnit}
              options={DISTANCE_UNIT_OPTIONS.map((option) => ({
                value: option.value,
                label: option.label,
              }))}
              onChange={(value) => setDistanceUnit(value === 'mi' ? 'mi' : 'km')}
              disabled={submitting}
            />
            <div className="vehicles-form__actions">
              <Button type="submit" variant="primary" disabled={submitting}>
                {submitting ? 'Saving…' : 'Save changes'}
              </Button>
            </div>
          </form>
        </section>

        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '0.5rem' }}>
            Role-based access
          </h2>
          <p className="app-page__subtitle" style={{ marginBottom: '1rem', fontSize: '0.875rem' }}>
            Read-only reference. Access enforced at login via JWT scopes.
          </p>
          <div className="vehicles-table-wrap">
            <table className="vehicles-table">
              <thead>
                <tr>
                  <th>Role</th>
                  {RBAC_MODULES.map((module) => (
                    <th key={module}>{module}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {RBAC_MATRIX_ROWS.map((row) => (
                  <tr key={row.role}>
                    <td>{row.role}</td>
                    {row.access.map((level, index) => (
                      <td key={`${row.role}-${RBAC_MODULES[index]}`}>
                        {level}
                      </td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </>
  )
}
