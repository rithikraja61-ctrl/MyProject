type CheckboxProps = {
  id: string
  label: string
  checked: boolean
  onChange: (checked: boolean) => void
  disabled?: boolean
}

export function Checkbox({
  id,
  label,
  checked,
  onChange,
  disabled = false,
}: CheckboxProps) {
  return (
    <label className="checkbox" htmlFor={id}>
      <input
        id={id}
        className="checkbox__input"
        type="checkbox"
        checked={checked}
        onChange={(event) => onChange(event.target.checked)}
        disabled={disabled}
      />
      <span className="checkbox__box" aria-hidden="true">
        <span className="checkbox__check">✓</span>
      </span>
      {label}
    </label>
  )
}
