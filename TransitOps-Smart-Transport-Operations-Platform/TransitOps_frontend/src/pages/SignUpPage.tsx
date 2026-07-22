import { useRef, useState } from 'react'
import { Navigate, useNavigate } from 'react-router-dom'
import { ApiError, postJson } from '../lib/api'
import { isAuthenticated } from '../lib/authStorage'
import { getErrorMessage, notifyError } from '../lib/notify'
import { AuthLayout } from '../sections/auth/AuthLayout'
import { SignUpFormSection } from '../sections/auth/SignUpFormSection'
import type { RoleName, SignupRequest, SignupResponse } from '../types/auth'

export function SignUpPage() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({})
  const submittingRef = useRef(false)

  if (isAuthenticated()) {
    return <Navigate to="/" replace />
  }

  async function handleSubmit(data: {
    username: string
    email: string
    password: string
    role: RoleName
  }) {
    if (submittingRef.current || loading) {
      return
    }

    submittingRef.current = true
    setLoading(true)
    setFieldErrors({})

    const payload: SignupRequest = {
      username: data.username,
      email: data.email,
      password: data.password,
      role: data.role,
    }

    try {
      await postJson<SignupResponse>('/api/auth/signup', payload)
      navigate('/signin?registered=1', { replace: true })
    } catch (err) {
      if (err instanceof ApiError && err.errors) {
        setFieldErrors(err.errors)
        const firstError = Object.values(err.errors)[0]
        notifyError(firstError ?? getErrorMessage(err, 'Unable to create account. Please try again.'))
      } else {
        notifyError(getErrorMessage(err, 'Unable to create account. Please try again.'))
      }
    } finally {
      setLoading(false)
      submittingRef.current = false
    }
  }

  return (
    <AuthLayout>
      <SignUpFormSection
        onSubmit={handleSubmit}
        loading={loading}
        fieldErrors={fieldErrors}
      />
    </AuthLayout>
  )
}
