import type { RoleName } from '../types/auth'

export type RoleOption = {
  value: RoleName
  label: string
}

export const ROLE_OPTIONS: RoleOption[] = [
  {
    value: 'FLEET_MANAGER',
    label: 'Fleet Manager',
  },
  {
    value: 'DISPATCHER',
    label: 'Dispatcher',
  },
  {
    value: 'SAFETY_OFFICER',
    label: 'Safety Officer',
  },
  {
    value: 'FINANCIAL_ANALYST',
    label: 'Financial Analyst',
  },
]

export const ROLE_LABELS: Record<RoleName, string> = Object.fromEntries(
  ROLE_OPTIONS.map((role) => [role.value, role.label]),
) as Record<RoleName, string>

export const DEFAULT_ROLE: RoleName = 'DISPATCHER'

export function getRoleLabel(role: RoleName): string {
  return ROLE_LABELS[role]
}
