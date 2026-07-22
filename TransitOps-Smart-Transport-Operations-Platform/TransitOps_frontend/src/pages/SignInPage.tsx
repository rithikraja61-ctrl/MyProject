import { useEffect, useRef, useState } from 'react'
import { Navigate, useNavigate, useSearchParams } from 'react-router-dom'
import { postJson } from '../lib/api'
import { getDefaultAppPath } from '../constants/nav'
import { getAuthSession, isAuthenticated, saveAuthSession } from '../lib/authStorage'
import { getErrorMessage, notifyError, notifySuccess } from '../lib/notify'
import { AuthLayout } from '../sections/auth/AuthLayout'
import { SignInFormSection } from '../sections/auth/SignInFormSection'
import type { RoleName, SigninRequest, SigninResponse } from '../types/auth'

export function SignInPage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [loading, setLoading] = useState(false)
  const registeredNotifiedRef = useRef(false)
  const submittingRef = useRef(false)

  const registered = searchParams.get('registered') === '1'

  useEffect(() => {
    if (registered && !registeredNotifiedRef.current) {
      registeredNotifiedRef.current = true
      notifySuccess('Account created successfully. Sign in to continue.')
    }
  }, [registered])

  if (isAuthenticated()) {
    const session = getAuthSession()
    return <Navigate to={session ? getDefaultAppPath(session.user.scopes) : '/'} replace />
  }

  async function handleSubmit(data: {
    email: string
    password: string
    role: RoleName
    rememberMe: boolean
  }) {
    if (submittingRef.current || loading) {
      return
    }

    submittingRef.current = true
    setLoading(true)

    const payload: SigninRequest = {
      email: data.email.trim(),
      password: data.password,
      role: data.role,
    }

    try {
      const response = await postJson<SigninResponse>('/api/auth/signin', payload)
      saveAuthSession(response, data.rememberMe)
      navigate(getDefaultAppPath(response.scopes), { replace: true })
    } catch (err) {
      const message = getErrorMessage(err, 'Unable to sign in. Please try again.')
      notifyError(message)
    } finally {
      setLoading(false)
      submittingRef.current = false
    }
  }

  return (
    <AuthLayout>
      <SignInFormSection onSubmit={handleSubmit} loading={loading} />
    </AuthLayout>
  )
}
