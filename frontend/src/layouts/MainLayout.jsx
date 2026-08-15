import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../components/common/Sidebar';
import Navbar from '../components/common/Navbar';

/**
 * Main Application Layout Component
 *
 * Enforces the fixed 280px left sidebar and top navbar header with dynamic container sizing.
 */
const MainLayout = ({ title }) => {
  return (
    <div className="min-h-screen bg-[#faf8ff] flex">
      {/* Fixed Left Sidebar */}
      <Sidebar />

      {/* Main Content Area Offset by Sidebar Width (280px / w-70) */}
      <div className="flex-1 ml-70 flex flex-col min-w-0">
        <Navbar title={title} />
        <main className="flex-1 p-8 max-w-[1440px] w-full mx-auto">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default MainLayout;
