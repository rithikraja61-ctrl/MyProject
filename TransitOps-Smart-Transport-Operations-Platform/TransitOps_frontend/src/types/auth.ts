export type RoleName =
  | 'FLEET_MANAGER'
  | 'DISPATCHER'
  | 'SAFETY_OFFICER'
  | 'FINANCIAL_ANALYST'

export type SessionResponse = {
  id: number
  username: string
  email: string
  role: RoleName
  scopes: string[]
}

export type SigninRequest = {
  email: string
  password: string
  role: RoleName
}

export type SigninResponse = {
  accessToken: string
  tokenType: string
  expiresIn: number
  id: number
  username: string
  email: string
  role: RoleName
  scopes: string[]
}

export type SignupRequest = {
  username: string
  email: string
  password: string
  role: RoleName
}

export type SignupResponse = {
  id: number
  username: string
  email: string
  role: RoleName
  scopes: string[]
}

export type ErrorResponse = {
  status: number
  error: string
  message: string
  errors?: Record<string, string>
}

export type AuthUser = {
  id: number
  username: string
  email: string
  role: RoleName
  scopes: string[]
}

export type AuthSession = {
  accessToken: string
  tokenType: string
  expiresIn: number
  user: AuthUser
}
