import type { ReactNode } from 'react'
import { BrandingSection } from './BrandingSection'

type AuthLayoutProps = {
  children: ReactNode
}

export function AuthLayout({ children }: AuthLayoutProps) {
  return (
    <div className="auth-layout">
      <aside className="auth-layout__branding">
        <BrandingSection />
      </aside>
      <main className="auth-layout__form">
        <div className="auth-layout__form-inner">{children}</div>
      </main>
    </div>
  )
}
