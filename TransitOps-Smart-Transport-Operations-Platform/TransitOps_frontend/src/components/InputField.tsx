type InputFieldProps = {
  id: string
  label: string
  type?: 'text' | 'email' | 'password' | 'date'
  value: string
  onChange: (value: string) => void
  error?: string
  autoComplete?: string
  disabled?: boolean
}

export function InputField({
  id,
  label,
  type = 'text',
  value,
  onChange,
  error,
  autoComplete,
  disabled = false,
}: InputFieldProps) {
  return (
    <div className="field">
      <label className="field__label" htmlFor={id}>
        {label}
      </label>
      <input
        id={id}
        className={`field__input${error ? ' field__input--error' : ''}`}
        type={type}
        value={value}
        onChange={(event) => onChange(event.target.value)}
        autoComplete={autoComplete}
        disabled={disabled}
        aria-invalid={error ? true : undefined}
        aria-describedby={error ? `${id}-error` : undefined}
      />
      {error ? (
        <p className="field__error" id={`${id}-error`}>
          {error}
        </p>
      ) : null}
    </div>
  )
}
