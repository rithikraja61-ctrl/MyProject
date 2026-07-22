import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Button } from '../../components/Button'
import { Checkbox } from '../../components/Checkbox'
import { InputField } from '../../components/InputField'
import { SelectField } from '../../components/SelectField'
import { DEFAULT_ROLE, ROLE_OPTIONS } from '../../constants/roles'
import type { RoleName } from '../../types/auth'

type SignInFormSectionProps = {
  onSubmit: (data: {
    email: string
    password: string
    role: RoleName
    rememberMe: boolean
  }) => Promise<void>
  loading?: boolean
}

export function SignInFormSection({
  onSubmit,
  loading = false,
}: SignInFormSectionProps) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [role, setRole] = useState<RoleName>(DEFAULT_ROLE)
  const [rememberMe, setRememberMe] = useState(true)

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    await onSubmit({ email, password, role, rememberMe })
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit} noValidate>
      <header className="auth-form__header">
        <h2 className="auth-form__title">Sign in to your account</h2>
        <p className="auth-form__subtitle">Enter your credentials to continue.</p>
      </header>

      <div className="auth-form__fields">
        <InputField
          id="signin-email"
          label="Email"
          type="email"
          value={email}
          onChange={setEmail}
          autoComplete="email"
          disabled={loading}
        />

        <InputField
          id="signin-password"
          label="Password"
          type="password"
          value={password}
          onChange={setPassword}
          autoComplete="current-password"
          disabled={loading}
        />

        <SelectField
          id="signin-role"
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

      <div className="auth-form__row">
        <Checkbox
          id="signin-remember"
          label="Remember me"
          checked={rememberMe}
          onChange={setRememberMe}
          disabled={loading}
        />
        <span className="auth-form__forgot" title="Not available yet">
          Forgot password?
        </span>
      </div>

      <div className="auth-form__actions">
        <Button type="submit" disabled={loading}>
          {loading ? 'Signing in…' : 'Sign In'}
        </Button>
      </div>

      <p className="auth-form__footer-link">
        Don&apos;t have an account? <Link to="/signup">Sign up</Link>
      </p>
    </form>
  )
}
