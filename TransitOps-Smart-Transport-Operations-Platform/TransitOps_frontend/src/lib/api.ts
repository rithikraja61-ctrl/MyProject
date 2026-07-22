import { getAuthSession } from './authStorage'
import type { ErrorResponse } from '../types/auth'

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8081'

export class ApiError extends Error {
  status: number
  errors?: Record<string, string>

  constructor(status: number, message: string, errors?: Record<string, string>) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.errors = errors
  }
}

async function parseErrorResponse(response: Response): Promise<ApiError> {
  let body: ErrorResponse | null = null

  try {
    body = (await response.json()) as ErrorResponse
  } catch {
    body = null
  }

  return new ApiError(
    response.status,
    body?.message ?? response.statusText,
    body?.errors,
  )
}

export async function postJson<T>(path: string, body: unknown): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })

  if (!response.ok) {
    throw await parseErrorResponse(response)
  }

  return response.json() as Promise<T>
}

export async function authFetch(
  path: string,
  options: RequestInit = {},
): Promise<Response> {
  const session = getAuthSession()
  const headers = new Headers(options.headers)

  if (session?.accessToken) {
    headers.set('Authorization', `Bearer ${session.accessToken}`)
  }

  try {
    return await fetch(`${API_BASE}${path}`, {
      ...options,
      headers,
    })
  } catch {
    throw new ApiError(
      0,
      `Cannot reach API at ${API_BASE}. Is the backend running on port 8081?`,
    )
  }
}

async function parseJsonResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    throw await parseErrorResponse(response)
  }

  if (response.status === 204) {
    return undefined as T
  }

  return response.json() as Promise<T>
}

export async function getJson<T>(path: string): Promise<T> {
  const response = await authFetch(path)
  return parseJsonResponse<T>(response)
}

export async function postAuthJson<T>(path: string, body: unknown): Promise<T> {
  const response = await authFetch(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
  return parseJsonResponse<T>(response)
}

export async function deleteAuth(path: string): Promise<void> {
  const response = await authFetch(path, { method: 'DELETE' })
  await parseJsonResponse<void>(response)
}

export async function downloadAuthFile(path: string, filename: string): Promise<void> {
  const response = await authFetch(path)
  if (!response.ok) {
    throw await parseErrorResponse(response)
  }

  const blob = await response.blob()
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  link.click()
  URL.revokeObjectURL(url)
}

export async function logout(): Promise<void> {
  const response = await authFetch('/api/auth/logout', { method: 'POST' })
  if (!response.ok) {
    throw await parseErrorResponse(response)
  }
}
