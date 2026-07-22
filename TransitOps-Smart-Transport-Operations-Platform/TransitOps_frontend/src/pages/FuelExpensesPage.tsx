import { type FormEvent, useCallback, useEffect, useState } from 'react'
import { Button } from '../components/Button'
import { InputField } from '../components/InputField'
import { SelectField } from '../components/SelectField'
import { ApiError, getJson, postAuthJson } from '../lib/api'
import { useSettings } from '../context/SettingsContext'
import { notifyError, notifySuccess } from '../lib/notify'
import { FUEL_VOLUME_UNIT } from '../constants/settings'
import { EXPENSE_TYPE_OPTIONS, type Expense } from '../types/expense'
import {
  ENTRY_TYPE_OPTIONS,
  type EntryType,
  type FuelLog,
  type FuelVehicleOption,
} from '../types/fuel'

function todayIso(): string {
  return new Date().toISOString().slice(0, 10)
}

type LogEntryForm = {
  entryType: EntryType
  vehicleId: number
  logDate: string
  liters: number
  cost: number
  expenseType: string
  description: string
  amount: number
}

function emptyForm(): LogEntryForm {
  return {
    entryType: 'fuel',
    vehicleId: 0,
    logDate: todayIso(),
    liters: 0,
    cost: 0,
    expenseType: '',
    description: '',
    amount: 0,
  }
}

export function FuelExpensesPage() {
  const { formatters } = useSettings()
  const [vehicles, setVehicles] = useState<FuelVehicleOption[]>([])
  const [fuelLogs, setFuelLogs] = useState<FuelLog[]>([])
  const [expenses, setExpenses] = useState<Expense[]>([])
  const [form, setForm] = useState<LogEntryForm>(emptyForm)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const [listError, setListError] = useState<string | null>(null)
  const [formError, setFormError] = useState<string | null>(null)
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({})

  const loadData = useCallback(async () => {
    setLoading(true)
    setListError(null)
    try {
      const [vehicleData, fuelData, expenseData] = await Promise.all([
        getJson<FuelVehicleOption[]>('/api/fuel/vehicle-options'),
        getJson<FuelLog[]>('/api/fuel'),
        getJson<Expense[]>('/api/expenses'),
      ])
      setVehicles(vehicleData)
      setFuelLogs(fuelData)
      setExpenses(expenseData)
    } catch (err) {
      const message = err instanceof ApiError ? err.message : 'Failed to load fuel and expenses'
      setListError(message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void loadData()
  }, [loadData])

  const vehicleOptions = [
    { value: '', label: 'Select vehicle' },
    ...vehicles.map((v) => ({
      value: String(v.id),
      label: `${v.registrationNumber} — ${v.name}`,
    })),
  ]

  function updateField<K extends keyof LogEntryForm>(key: K, value: LogEntryForm[K]) {
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

  function handleEntryTypeChange(value: string) {
    const entryType = value as EntryType
    setForm((prev) => ({ ...prev, entryType }))
    setFieldErrors((prev) => {
      const next = { ...prev }
      delete next.liters
      delete next.cost
      delete next.expenseType
      delete next.description
      delete next.amount
      return next
    })
    setFormError(null)
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

    if (form.entryType === 'expense' && !form.expenseType) {
      setFormError('Select an expense type')
      return
    }

    setSubmitting(true)
    setFieldErrors({})
    setFormError(null)

    try {
      if (form.entryType === 'fuel') {
        await postAuthJson<FuelLog>('/api/fuel', {
          vehicleId: Number(form.vehicleId),
          logDate: form.logDate,
          liters: Number(form.liters),
          cost: Number(form.cost),
        })
        notifySuccess('Fuel logged')
      } else {
        await postAuthJson<Expense>('/api/expenses', {
          vehicleId: Number(form.vehicleId),
          logDate: form.logDate,
          type: form.expenseType,
          description: form.description || undefined,
          amount: Number(form.amount),
        })
        notifySuccess('Expense logged')
      }

      setForm(emptyForm())
      await loadData()
    } catch (err) {
      if (err instanceof ApiError && err.errors) {
        setFieldErrors(err.errors)
        notifyError('Please fix the highlighted fields')
      } else {
        const message =
          err instanceof ApiError
            ? err.message
            : form.entryType === 'fuel'
              ? 'Failed to log fuel'
              : 'Failed to log expense'
        setFormError(message)
        notifyError(message)
      }
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <>
      <header className="app-page__header">
        <h1 className="app-page__title">Fuel &amp; Expenses</h1>
        <p className="app-page__subtitle">Track fuel usage and operational costs per vehicle</p>
      </header>

      <div className="vehicles-layout">
        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Log entry
          </h2>
          {formError ? <p className="app-error">{formError}</p> : null}
          <form className="vehicles-form" onSubmit={handleSubmit}>
            <SelectField
              id="entry-type"
              label="Entry type"
              value={form.entryType}
              options={ENTRY_TYPE_OPTIONS}
              onChange={handleEntryTypeChange}
              disabled={submitting}
            />
            <SelectField
              id="entry-vehicle"
              label="Vehicle"
              value={form.vehicleId ? String(form.vehicleId) : ''}
              options={vehicleOptions}
              onChange={(value) => updateField('vehicleId', Number(value) || 0)}
              error={fieldErrors.vehicleId}
              disabled={submitting}
            />
            <InputField
              id="entry-date"
              label="Date"
              type="date"
              value={form.logDate}
              onChange={(value) => updateField('logDate', value)}
              error={fieldErrors.logDate}
              disabled={submitting}
            />
            {form.entryType === 'fuel' ? (
              <>
                <InputField
                  id="entry-liters"
                  label={`Liters (${FUEL_VOLUME_UNIT})`}
                  value={form.liters ? String(form.liters) : ''}
                  onChange={(value) => updateField('liters', Number(value) || 0)}
                  error={fieldErrors.liters}
                  disabled={submitting}
                />
                <InputField
                  id="entry-cost"
                  label={`Cost (${formatters.currencySymbol})`}
                  value={form.cost ? String(form.cost) : ''}
                  onChange={(value) => updateField('cost', Number(value) || 0)}
                  error={fieldErrors.cost}
                  disabled={submitting}
                />
              </>
            ) : (
              <>
                <SelectField
                  id="entry-expense-type"
                  label="Type"
                  value={form.expenseType}
                  options={EXPENSE_TYPE_OPTIONS}
                  onChange={(value) => updateField('expenseType', value)}
                  error={fieldErrors.type ?? fieldErrors.expenseType}
                  disabled={submitting}
                />
                <InputField
                  id="entry-description"
                  label="Description"
                  value={form.description}
                  onChange={(value) => updateField('description', value)}
                  error={fieldErrors.description}
                  disabled={submitting}
                />
                <InputField
                  id="entry-amount"
                  label={`Amount (${formatters.currencySymbol})`}
                  value={form.amount ? String(form.amount) : ''}
                  onChange={(value) => updateField('amount', Number(value) || 0)}
                  error={fieldErrors.amount}
                  disabled={submitting}
                />
              </>
            )}
            <div className="vehicles-form__actions">
              <Button type="submit" variant="primary" disabled={submitting}>
                {submitting ? 'Saving…' : 'Log entry'}
              </Button>
            </div>
          </form>
        </section>

        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Fuel log
          </h2>
          {loading ? <p className="app-loading">Loading fuel logs…</p> : null}
          {listError ? <p className="app-error">{listError}</p> : null}
          {!loading && !listError && fuelLogs.length === 0 ? (
            <p className="app-card--empty">No fuel records yet.</p>
          ) : null}
          {!loading && fuelLogs.length > 0 ? (
            <div className="vehicles-table-wrap">
              <table className="vehicles-table">
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Vehicle</th>
                    <th>Liters ({FUEL_VOLUME_UNIT})</th>
                    <th>Cost ({formatters.currencySymbol})</th>
                  </tr>
                </thead>
                <tbody>
                  {fuelLogs.map((log) => (
                    <tr key={log.id}>
                      <td>{log.logDate}</td>
                      <td>{log.vehicleRegistration}</td>
                      <td>{formatters.formatLiters(log.liters)}</td>
                      <td>{formatters.formatCurrency(log.cost)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : null}
        </section>

        <section className="app-card">
          <h2 className="app-page__title" style={{ fontSize: '1.125rem', marginBottom: '1rem' }}>
            Expense log
          </h2>
          {loading ? <p className="app-loading">Loading expenses…</p> : null}
          {!loading && !listError && expenses.length === 0 ? (
            <p className="app-card--empty">No expense records yet.</p>
          ) : null}
          {!loading && expenses.length > 0 ? (
            <div className="vehicles-table-wrap">
              <table className="vehicles-table">
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Vehicle</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th>Amount ({formatters.currencySymbol})</th>
                  </tr>
                </thead>
                <tbody>
                  {expenses.map((expense) => (
                    <tr key={expense.id}>
                      <td>{expense.logDate}</td>
                      <td>{expense.vehicleRegistration}</td>
                      <td>{expense.type}</td>
                      <td>{expense.description ?? '—'}</td>
                      <td>{formatters.formatCurrency(expense.amount)}</td>
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
