import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'
import { ProtectedRoute } from './components/ProtectedRoute'
import { ScopeRoute } from './components/ScopeRoute'
import { APP_NAV_ITEMS } from './constants/nav'
import { SettingsProvider } from './context/SettingsContext'
import { AnalyticsPage } from './pages/AnalyticsPage'
import { DashboardPage } from './pages/DashboardPage'
import { DriversPage } from './pages/DriversPage'
import { FuelExpensesPage } from './pages/FuelExpensesPage'
import { MaintenancePage } from './pages/MaintenancePage'
import { SettingsPage } from './pages/SettingsPage'
import { SignInPage } from './pages/SignInPage'
import { SignUpPage } from './pages/SignUpPage'
import { TripsPage } from './pages/TripsPage'
import { VehiclesPage } from './pages/VehiclesPage'
import { AppShell } from './sections/app/AppShell'

const dashboardScopes = APP_NAV_ITEMS.find((item) => item.path === '/')!.scopes
const fleetScopes = APP_NAV_ITEMS.find((item) => item.path === '/vehicles')!.scopes
const driversScopes = APP_NAV_ITEMS.find((item) => item.path === '/drivers')!.scopes
const tripsScopes = APP_NAV_ITEMS.find((item) => item.path === '/trips')!.scopes
const maintenanceScopes = APP_NAV_ITEMS.find((item) => item.path === '/maintenance')!.scopes
const fuelScopes = APP_NAV_ITEMS.find((item) => item.path === '/fuel-expenses')!.scopes
const analyticsScopes = APP_NAV_ITEMS.find((item) => item.path === '/analytics')!.scopes

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signin" element={<SignInPage />} />
        <Route path="/signup" element={<SignUpPage />} />
        <Route
          element={
            <ProtectedRoute>
              <SettingsProvider>
                <AppShell />
              </SettingsProvider>
            </ProtectedRoute>
          }
        >
          <Route
            path="/"
            element={
              <ScopeRoute scopes={dashboardScopes}>
                <DashboardPage />
              </ScopeRoute>
            }
          />
          <Route
            path="/vehicles"
            element={
              <ScopeRoute scopes={fleetScopes}>
                <VehiclesPage />
              </ScopeRoute>
            }
          />
          <Route
            path="/drivers"
            element={
              <ScopeRoute scopes={driversScopes}>
                <DriversPage />
              </ScopeRoute>
            }
          />
          <Route
            path="/trips"
            element={
              <ScopeRoute scopes={tripsScopes}>
                <TripsPage />
              </ScopeRoute>
            }
          />
          <Route
            path="/maintenance"
            element={
              <ScopeRoute scopes={maintenanceScopes}>
                <MaintenancePage />
              </ScopeRoute>
            }
          />
          <Route
            path="/fuel-expenses"
            element={
              <ScopeRoute scopes={fuelScopes}>
                <FuelExpensesPage />
              </ScopeRoute>
            }
          />
          <Route
            path="/analytics"
            element={
              <ScopeRoute scopes={analyticsScopes}>
                <AnalyticsPage />
              </ScopeRoute>
            }
          />
          <Route path="/settings" element={<SettingsPage />} />
        </Route>
        <Route path="*" element={<Navigate to="/signin" replace />} />
      </Routes>
      <ToastContainer
        position="top-right"
        autoClose={4000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        pauseOnHover
        theme="dark"
        toastClassName="transitops-toast"
      />
    </BrowserRouter>
  )
}

export default App
