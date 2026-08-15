import React from 'react';
import { Outlet } from 'react-router-dom';

/**
 * Authentication Layout Component
 *
 * Full-height container for auth flows.
 */
const AuthLayout = () => {
  return (
    <div className="min-h-screen bg-[#faf8ff] flex items-center justify-center">
      <Outlet />
    </div>
  );
};

export default AuthLayout;
