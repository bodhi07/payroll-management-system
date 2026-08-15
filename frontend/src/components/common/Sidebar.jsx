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
  HiOutlineShieldCheck,
  HiOutlineClock,
  HiOutlineArrowPath,
  HiOutlineArrowRightOnRectangle,
  HiOutlineUserGroup
} from 'react-icons/hi2';
import { useAuth } from '../../hooks/useAuth';
import { useNotificationContext } from '../../context/NotificationContext';
import { ROUTES } from '../../constants/routes';

/**
 * Sidebar Navigation Component
 *
 * Fixed 280px sidebar displaying GlobalPay branding, all 6 member modules,
 * dedicated Member 06 Security & RBAC section, and quick actions.
 */
const Sidebar = () => {
  const navigate = useNavigate();
  const { logout, user } = useAuth();
  const { showToastSuccess, showConfirmDialog } = useNotificationContext();

  const coreModules = [
    { name: 'Dashboard', path: ROUTES.DASHBOARD, icon: HiOutlineSquares2X2, tag: null },
    { name: 'Employees', path: ROUTES.EMPLOYEES, icon: HiOutlineUsers, tag: 'M1' },
    { name: 'Attendance', path: ROUTES.ATTENDANCE, icon: HiOutlineCalendar, tag: 'M2' },
    { name: 'Payroll', path: ROUTES.PAYROLL, icon: HiOutlineBanknotes, tag: 'M3' },
    { name: 'Leave Management', path: ROUTES.LEAVE, icon: HiOutlineDocumentCheck, tag: 'M4' },
    { name: 'Departments', path: ROUTES.DEPARTMENTS, icon: HiOutlineBuildingOffice2, tag: 'M5' },
    { name: 'Reports Center', path: ROUTES.REPORTS, icon: HiOutlineChartBar, tag: null },
  ];

  const member6SecurityModules = [
    { name: 'User Management (RBAC)', path: ROUTES.USER_MANAGEMENT, icon: HiOutlineUserGroup, tag: 'M6' },
    { name: 'Audit Logs & Security', path: ROUTES.AUDIT_LOGS, icon: HiOutlineClock, tag: 'M6' },
    { name: 'System Settings', path: ROUTES.SETTINGS, icon: HiOutlineCog6Tooth, tag: null },
  ];

  const handleLogout = async () => {
    const confirmed = await showConfirmDialog({
      title: 'Sign Out?',
      text: 'Are you sure you want to log out of your enterprise session?',
      confirmButtonText: 'Yes, Sign Out',
    });

    if (confirmed) {
      await logout();
      showToastSuccess('You have been logged out successfully.');
      navigate(ROUTES.LOGIN);
    }
  };

  return (
    <aside className="w-72 min-h-screen bg-[#faf8ff] border-r border-[#e1e2ed] flex flex-col justify-between p-5 fixed left-0 top-0 z-30 overflow-y-auto">
      <div>
        {/* Brand Logo Section */}
        <div className="flex items-center gap-3 mb-6 px-2">
          <div className="w-10 h-10 bg-[#004ac6] rounded-xl flex items-center justify-center text-white shadow-md">
            <HiOutlineBanknotes className="w-6 h-6" />
          </div>
          <div>
            <h1 className="font-heading font-extrabold text-xl text-[#191b23] leading-none">GlobalPay</h1>
            <p className="text-[10px] font-bold tracking-widest text-[#004ac6] uppercase mt-1">Enterprise HRMS</p>
          </div>
        </div>

        {/* Core Modules List */}
        <div className="mb-6">
          <p className="px-3 mb-2 text-[10px] font-extrabold uppercase tracking-widest text-[#737686]">
            Core HRMS Modules
          </p>
          <nav className="space-y-1">
            {coreModules.map((item) => {
              const Icon = item.icon;
              return (
                <NavLink
                  key={item.name}
                  to={item.path}
                  className={({ isActive }) =>
                    `flex items-center justify-between px-3.5 py-2.5 rounded-xl text-xs font-semibold transition-all ${
                      isActive
                        ? 'bg-[#004ac6] text-white shadow-sm'
                        : 'text-[#434655] hover:bg-[#ededf9] hover:text-[#191b23]'
                    }`
                  }
                >
                  <div className="flex items-center gap-3">
                    <Icon className="w-4 h-4 flex-shrink-0" />
                    <span>{item.name}</span>
                  </div>
                  {item.tag && (
                    <span className="text-[9px] font-mono font-bold px-1.5 py-0.5 rounded bg-black/10 text-inherit">
                      {item.tag}
                    </span>
                  )}
                </NavLink>
              );
            })}
          </nav>
        </div>

        {/* Member 06 Security & Administration Section */}
        <div>
          <div className="flex items-center justify-between px-3 mb-2">
            <p className="text-[10px] font-extrabold uppercase tracking-widest text-[#943700]">
              Member 06 Security & RBAC
            </p>
            <span className="text-[9px] font-bold px-1.5 py-0.2 bg-[#ffede6] text-[#943700] rounded-md">
              ADMIN
            </span>
          </div>
          <nav className="space-y-1">
            {member6SecurityModules.map((item) => {
              const Icon = item.icon;
              return (
                <NavLink
                  key={item.name}
                  to={item.path}
                  className={({ isActive }) =>
                    `flex items-center justify-between px-3.5 py-2.5 rounded-xl text-xs font-semibold transition-all ${
                      isActive
                        ? 'bg-[#191b23] text-white shadow-sm'
                        : 'text-[#434655] hover:bg-[#ededf9] hover:text-[#191b23]'
                    }`
                  }
                >
                  <div className="flex items-center gap-3">
                    <Icon className="w-4 h-4 flex-shrink-0" />
                    <span>{item.name}</span>
                  </div>
                  {item.tag && (
                    <span className="text-[9px] font-mono font-bold px-1.5 py-0.5 rounded bg-[#ffede6] text-[#943700]">
                      {item.tag}
                    </span>
                  )}
                </NavLink>
              );
            })}
          </nav>
        </div>
      </div>

      {/* Bottom Actions Section */}
      <div className="pt-4 border-t border-[#e1e2ed]/60 space-y-2 mt-4">
        {user && (
          <div className="p-2.5 bg-[#f3f3fe] rounded-xl border border-[#e1e2ed] flex items-center gap-2.5">
            <div className="w-8 h-8 rounded-full bg-[#004ac6] text-white font-bold text-xs flex items-center justify-center">
              {user.username?.charAt(0).toUpperCase()}
            </div>
            <div className="overflow-hidden">
              <p className="font-bold text-xs text-[#191b23] truncate">{user.username}</p>
              <p className="text-[10px] text-[#737686] truncate">{user.roles?.join(', ')}</p>
            </div>
          </div>
        )}

        <button
          onClick={() => navigate(ROUTES.PAYROLL)}
          className="w-full py-2.5 px-4 bg-[#004ac6] hover:bg-[#2563eb] text-white font-semibold text-xs rounded-xl flex items-center justify-center gap-2 shadow-md transition-all active:scale-[0.98]"
        >
          <HiOutlineArrowPath className="w-4 h-4" />
          <span>Quick Process Payroll</span>
        </button>

        <button
          onClick={handleLogout}
          className="w-full py-2 px-4 bg-[#fee2e2]/60 hover:bg-[#fee2e2] text-[#991b1b] font-semibold text-xs rounded-xl flex items-center justify-center gap-2 transition-colors"
        >
          <HiOutlineArrowRightOnRectangle className="w-4 h-4" />
          <span>Sign Out Session</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
