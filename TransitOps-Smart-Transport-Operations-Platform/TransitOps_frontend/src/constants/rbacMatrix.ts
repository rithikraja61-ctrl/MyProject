// ponytail: static mirror of RoleScopeDefinitions — update both if scopes change

export const RBAC_MODULES = [
  'Dashboard',
  'Fleet',
  'Drivers',
  'Trips',
  'Maintenance',
  'Fuel & Exp',
  'Analytics',
] as const

export type RbacAccessLevel = '—' | 'View' | 'CRUD'

export type RbacMatrixRow = {
  role: string
  access: RbacAccessLevel[]
}

export const RBAC_MATRIX_ROWS: RbacMatrixRow[] = [
  {
    role: 'Fleet Manager',
    access: ['View', 'CRUD', 'View', '—', 'CRUD', '—', 'View'],
  },
  {
    role: 'Dispatcher',
    access: ['View', 'View', '—', 'CRUD', '—', '—', '—'],
  },
  {
    role: 'Safety Officer',
    access: ['View', '—', 'CRUD', 'View', '—', '—', '—'],
  },
  {
    role: 'Financial Analyst',
    access: ['View', 'View', '—', '—', '—', 'CRUD', 'View'],
  },
]
