import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import MainLayout from '../layouts/MainLayout';
import AuthLayout from '../layouts/AuthLayout';

import Login from '../pages/Login';
import Register from '../pages/Register';
import Dashboard from '../pages/Dashboard';
import Employees from '../pages/Employees';
import Attendance from '../pages/Attendance';
import Payroll from '../pages/Payroll';
import LeaveManagement from '../pages/LeaveManagement';
import Departments from '../pages/Departments';
import Reports from '../pages/Reports';
import Settings from '../pages/Settings';

import { ROUTES } from '../constants/routes';

/**
 * Main Application Routes Assembly
 *
 * Configures client routes using React Router DOM v7.
 * Encapsulates public auth routes (Login, Register) and protected dashboard views with MainLayout.
 */
const AppRoutes = () => {
  return (
    <Routes>
      {/* Public Auth Routes */}
      <Route element={<AuthLayout />}>
        <Route path={ROUTES.LOGIN} element={<Login />} />
        <Route path={ROUTES.REGISTER} element={<Register />} />
      </Route>

      {/* Protected Dashboard Routes */}
      <Route element={<ProtectedRoute />}>
        <Route element={<MainLayout title="Payroll Management" />}>
          <Route path={ROUTES.DASHBOARD} element={<Dashboard />} />
          <Route path={ROUTES.EMPLOYEES} element={<Employees />} />
          <Route path={ROUTES.ATTENDANCE} element={<Attendance />} />
          <Route path={ROUTES.PAYROLL} element={<Payroll />} />
          <Route path={ROUTES.LEAVE} element={<LeaveManagement />} />
          <Route path={ROUTES.DEPARTMENTS} element={<Departments />} />
          <Route path={ROUTES.REPORTS} element={<Reports />} />
          <Route path={ROUTES.SETTINGS} element={<Settings />} />
        </Route>
      </Route>

      {/* Default Catch-all Redirect */}
      <Route path="*" element={<Navigate to={ROUTES.DASHBOARD} replace />} />
    </Routes>
  );
};

export default AppRoutes;
