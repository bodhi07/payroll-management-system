import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { ROUTES } from '../constants/routes';

/**
 * Role-Based Route Guard
 *
 * Enforces role authorization (ADMIN, HR, EMPLOYEE) for restricted application pages.
 */
const RoleBasedRoute = ({ allowedRoles = [] }) => {
  const { user } = useAuth();

  if (!user || (allowedRoles.length > 0 && !allowedRoles.includes(user.role))) {
    return <Navigate to={ROUTES.DASHBOARD} replace />;
  }

  return <Outlet />;
};

export default RoleBasedRoute;
