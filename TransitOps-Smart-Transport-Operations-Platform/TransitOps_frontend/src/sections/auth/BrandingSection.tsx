import { ROLE_OPTIONS } from '../../constants/roles'

export function BrandingSection() {
  return (
    <>
      <header className="branding__header">
        <div className="branding__logo" aria-hidden="true" />
        <div>
          <h1 className="branding__title">TransitOps</h1>
          <p className="branding__tagline">Smart Transport Operations Platform</p>
        </div>
      </header>

      <div className="branding__content">
        <h2 className="branding__roles-title">One login, four roles:</h2>
        <ul className="branding__roles-list">
          {ROLE_OPTIONS.map((role) => (
            <li key={role.value}>{role.label}</li>
          ))}
        </ul>
      </div>

      <footer className="branding__footer">TRANSITOPS © 2026 · RBAC v1.0</footer>
    </>
  )
}
