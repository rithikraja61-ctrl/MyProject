type SelectOption = {
  value: string
  label: string
}

type SelectFieldProps = {
  id: string
  label: string
  value: string
  options: SelectOption[]
  onChange: (value: string) => void
  error?: string
  disabled?: boolean
}

export function SelectField({
  id,
  label,
  value,
  options,
  onChange,
  error,
  disabled = false,
}: SelectFieldProps) {
  return (
    <div className="field">
      <label className="field__label" htmlFor={id}>
        {label}
      </label>
      <select
        id={id}
        className={`field__select${error ? ' field__select--error' : ''}`}
        value={value}
        onChange={(event) => onChange(event.target.value)}
        disabled={disabled}
        aria-invalid={error ? true : undefined}
        aria-describedby={error ? `${id}-error` : undefined}
      >
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      {error ? (
        <p className="field__error" id={`${id}-error`}>
          {error}
        </p>
      ) : null}
    </div>
  )
}
