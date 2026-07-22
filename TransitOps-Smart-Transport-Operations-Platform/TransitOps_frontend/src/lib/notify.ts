import { toast } from 'react-toastify'

const ERROR_TOAST_ID = 'transitops-error'
const SUCCESS_TOAST_ID = 'transitops-success'

export function notifyError(message: string) {
  toast.error(message, { toastId: ERROR_TOAST_ID })
}

export function notifySuccess(message: string) {
  toast.success(message, { toastId: SUCCESS_TOAST_ID })
}

export function getErrorMessage(err: unknown, fallback: string): string {
  if (err instanceof Error && err.message) {
    return err.message
  }
  return fallback
}
