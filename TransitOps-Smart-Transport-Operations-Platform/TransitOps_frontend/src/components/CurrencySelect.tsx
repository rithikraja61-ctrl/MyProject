import { useEffect, useMemo, useRef, useState } from 'react'
import {
  CURRENCY_OPTIONS,
  currencySelectLabel,
  findCurrency,
} from '../constants/currencies'

type CurrencySelectProps = {
  id: string
  label: string
  value: string
  onChange: (code: string) => void
  disabled?: boolean
  error?: string
}

export function CurrencySelect({
  id,
  label,
  value,
  onChange,
  disabled = false,
  error,
}: CurrencySelectProps) {
  const [query, setQuery] = useState('')
  const [open, setOpen] = useState(false)
  const containerRef = useRef<HTMLDivElement>(null)

  const selected = findCurrency(value)

  const filtered = useMemo(() => {
    const term = query.trim().toLowerCase()
    if (!term) {
      return CURRENCY_OPTIONS
    }
    return CURRENCY_OPTIONS.filter(
      (currency) =>
        currency.code.toLowerCase().includes(term) ||
        currency.name.toLowerCase().includes(term) ||
        currency.symbol.toLowerCase().includes(term),
    )
  }, [query])

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (!containerRef.current?.contains(event.target as Node)) {
        setOpen(false)
        setQuery('')
      }
    }

    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  function selectCurrency(code: string) {
    onChange(code)
    setOpen(false)
    setQuery('')
  }

  return (
    <div className="field currency-select" ref={containerRef}>
      <label className="field__label" htmlFor={id}>
        {label}
      </label>
      <button
        type="button"
        id={id}
        className={`field__select currency-select__trigger${error ? ' field__select--error' : ''}`}
        onClick={() => !disabled && setOpen((prev) => !prev)}
        disabled={disabled}
        aria-expanded={open}
        aria-haspopup="listbox"
      >
        {currencySelectLabel(selected)}
      </button>
      {open ? (
        <div className="currency-select__panel">
          <input
            className="currency-select__search"
            type="search"
            placeholder="Search currency…"
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            autoFocus
          />
          <ul className="currency-select__list" role="listbox">
            {filtered.length === 0 ? (
              <li className="currency-select__empty">No currencies found</li>
            ) : (
              filtered.map((currency) => (
                <li key={currency.code}>
                  <button
                    type="button"
                    className={`currency-select__option${currency.code === value ? ' currency-select__option--active' : ''}`}
                    onClick={() => selectCurrency(currency.code)}
                    role="option"
                    aria-selected={currency.code === value}
                  >
                    {currencySelectLabel(currency)}
                  </button>
                </li>
              ))
            )}
          </ul>
        </div>
      ) : null}
      {error ? (
        <p className="field__error" id={`${id}-error`}>
          {error}
        </p>
      ) : null}
    </div>
  )
}
