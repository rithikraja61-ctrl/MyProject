import { logout } from './api'
import { clearAuthSession } from './authStorage'

export async function signOut(): Promise<void> {
  try {
    await logout()
  } catch {
    // Always clear client session even if server revoke fails (expired token, network)
  } finally {
    clearAuthSession()
  }
}
