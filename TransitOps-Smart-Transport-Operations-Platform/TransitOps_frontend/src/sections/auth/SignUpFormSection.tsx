import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../../components/Button'
import { InputField } from '../../components/InputField'
import { SelectField } from '../../components/SelectField'
import { notifyError } from '../../lib/notify'
import { DEFAULT_ROLE, ROLE_OPTIONS } from '../../constants/roles'
import type { RoleName } from '../../types/auth'

type SignUpFormSectionProps = {
  onSubmit: (data: {
    username: string
    email: string
    password: string
    role: RoleName
  }) => Promise<void>
  loading?: boolean
  fieldErrors?: Record<string, string>
}

const USERNAME_PATTERN = /^[a-zA-Z0-9_]+$/
const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
const MIN_CREDENTIAL_LENGTH = 8
const MAX_CREDENTIAL_LENGTH = 100

function validateSignUp(data: {
  username: string
  email: string
  credential: string
}): Record<string, string> {
  const errors: Record<string, string> = {}

  if (data.username.trim().length < 3 || data.username.trim().length > 50) {
    errors.username = 'Username must be between 3 and 50 characters'
  } else if (!USERNAME_PATTERN.test(data.username.trim())) {
    errors.username = 'Username may only contain letters, numbers, and underscores'
  }

  if (!data.email.trim()) {
    errors.email = 'Email is required'
  } else if (!EMAIL_PATTERN.test(data.email.trim())) {
    errors.email = 'Email must be a valid address'
  }

  if (
    data.credential.length < MIN_CREDENTIAL_LENGTH ||
    data.credential.length > MAX_CREDENTIAL_LENGTH
  ) {
    errors.password = `Credential must be between ${MIN_CREDENTIAL_LENGTH} and ${MAX_CREDENTIAL_LENGTH} characters`
  }

  return errors
}

export function SignUpFormSection({
  onSubmit,
  loading = false,
  fieldErrors = {},
}: SignUpFormSectionProps) {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [credential, setCredential] = useState('')
  const [role, setRole] = useState<RoleName>(DEFAULT_ROLE)
  const [clientErrors, setClientErrors] = useState<Record<string, string>>({})

  const errors = { ...clientErrors, ...fieldErrors }

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const validationErrors = validateSignUp({ username, email, credential })
    setClientErrors(validationErrors)

    if (Object.keys(validationErrors).length > 0) {
      notifyError('Please fix the highlighted fields.')
      return
    }

    await onSubmit({
      username: username.trim(),
      email: email.trim(),
      password: credential,
      role,
    })
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit} noValidate>
      <header className="auth-form__header">
        <h2 className="auth-form__title">Create your account</h2>
        <p className="auth-form__subtitle">
          Register with your role to access scoped modules.
        </p>
      </header>

      <div className="auth-form__fields">
        <InputField
          id="signup-username"
          label="Username"
          value={username}
          onChange={setUsername}
          error={errors.username}
          autoComplete="username"
          disabled={loading}
        />

        <InputField
          id="signup-email"
          label="Email"
          type="email"
          value={email}
          onChange={setEmail}
          error={errors.email}
          autoComplete="email"
          disabled={loading}
        />

        <InputField
          id="signup-password"
          label="Password"
          type="password"
          value={credential}
          onChange={setCredential}
          error={errors.password}
          autoComplete="new-password"
          disabled={loading}
        />

        <SelectField
          id="signup-role"
          label="Role (RBAC)"
          value={role}
          options={ROLE_OPTIONS.map((option) => ({
            value: option.value,
            label: option.label,
          }))}
          onChange={(value) => setRole(value as RoleName)}
          disabled={loading}
        />
      </div>

      <div className="auth-form__actions">
        <Button type="submit" disabled={loading}>
          {loading ? 'Creating account…' : 'Create account'}
        </Button>
      </div>

      <p className="auth-form__footer-link">
        Already have an account? <Link to="/signin">Sign in</Link>
      </p>
    </form>
  )
}
