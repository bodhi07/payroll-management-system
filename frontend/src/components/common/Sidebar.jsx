import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { 
  HiOutlineSquares2X2, 
  HiOutlineUsers, 
  HiOutlineCalendar, 
  HiOutlineBanknotes, 
  HiOutlineDocumentCheck, 
  HiOutlineBuildingOffice2, 
  HiOutlineChartBar, 
  HiOutlineCog6Tooth,
  HiOutlineClock,
  HiOutlineArrowPath,
  HiOutlineArrowRightOnRectangle
} from 'react-icons/hi2';
import { useAuth } from '../../hooks/useAuth';
import { useNotificationContext } from '../../context/NotificationContext';
import { ROUTES } from '../../constants/routes';

/**
 * Sidebar Navigation Component
 *
 * Fixed 280px sidebar displaying GlobalPay branding, primary module navigation,
 * active routing states, "Process Payroll" action, and Logout button.
 */
const Sidebar = () => {
  const navigate = useNavigate();
  const { logout } = useAuth();
  const { showToastSuccess, showConfirmDialog } = useNotificationContext();

  const navItems = [
    { name: 'Dashboard', path: ROUTES.DASHBOARD, icon: HiOutlineSquares2X2 },
    { name: 'Employees', path: ROUTES.EMPLOYEES, icon: HiOutlineUsers },
    { name: 'Attendance', path: ROUTES.ATTENDANCE, icon: HiOutlineCalendar },
    { name: 'Payroll', path: ROUTES.PAYROLL, icon: HiOutlineBanknotes },
    { name: 'Leave Management', path: ROUTES.LEAVE, icon: HiOutlineDocumentCheck },
    { name: 'Departments', path: ROUTES.DEPARTMENTS, icon: HiOutlineBuildingOffice2 },
    { name: 'Reports', path: ROUTES.REPORTS, icon: HiOutlineChartBar },
    { name: 'Settings', path: ROUTES.SETTINGS, icon: HiOutlineCog6Tooth },
    { name: 'Audit Logs', path: '/audit-logs', icon: HiOutlineClock },
  ];

  const handleLogout = async () => {
    const confirmed = await showConfirmDialog({
      title: 'Sign Out?',
      text: 'Are you sure you want to log out of your session?',
      confirmButtonText: 'Yes, Sign Out',
    });

    if (confirmed) {
      await logout();
      showToastSuccess('You have been logged out successfully.');
      navigate(ROUTES.LOGIN);
    }
  };

  return (
    <aside className="w-70 min-h-screen bg-[#faf8ff] border-r border-[#e1e2ed] flex flex-col justify-between p-6 fixed left-0 top-0 z-30">
      <div>
        {/* Brand Logo Section */}
        <div className="flex items-center gap-3 mb-8 px-2">
          <div className="w-10 h-10 bg-[#004ac6] rounded-xl flex items-center justify-center text-white shadow-md">
            <HiOutlineBanknotes className="w-6 h-6" />
          </div>
          <div>
            <h1 className="font-heading font-extrabold text-xl text-[#191b23] leading-none">GlobalPay</h1>
            <p className="text-[11px] font-medium tracking-wide text-[#737686] uppercase mt-1">Enterprise HRMS</p>
          </div>
        </div>

        {/* Navigation Items */}
        <nav className="space-y-1.5">
          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.name}
                to={item.path}
                className={({ isActive }) =>
                  `flex items-center gap-3.5 px-4 py-3 rounded-xl text-sm font-medium transition-all duration-150 ${
                    isActive
                      ? 'bg-[#dbe1ff] text-[#00174b] font-semibold shadow-xs'
                      : 'text-[#434655] hover:bg-[#ededf9] hover:text-[#191b23]'
                  }`
                }
              >
                <Icon className="w-5 h-5 flex-shrink-0" />
                <span>{item.name}</span>
              </NavLink>
            );
          })}
        </nav>
      </div>

      {/* Bottom Actions Section */}
      <div className="pt-4 border-t border-[#e1e2ed]/60 space-y-2">
        <button
          onClick={() => navigate(ROUTES.PAYROLL)}
          className="w-full py-3 px-4 bg-[#004ac6] hover:bg-[#2563eb] text-white font-medium text-sm rounded-xl flex items-center justify-center gap-2 shadow-md transition-all active:scale-[0.98]"
        >
          <HiOutlineArrowPath className="w-4 h-4" />
          <span>Process Payroll</span>
        </button>

        <button
          onClick={handleLogout}
          className="w-full py-2.5 px-4 bg-[#fee2e2]/60 hover:bg-[#fee2e2] text-[#991b1b] font-semibold text-xs rounded-xl flex items-center justify-center gap-2 transition-colors"
        >
          <HiOutlineArrowRightOnRectangle className="w-4 h-4" />
          <span>Sign Out</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
