import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  HiOutlineMagnifyingGlass, 
  HiOutlineBell, 
  HiOutlineCog6Tooth,
  HiOutlineArrowRightOnRectangle,
  HiOutlineCheck,
  HiOutlineInformationCircle
} from 'react-icons/hi2';
import { useAuth } from '../../hooks/useAuth';
import { useNotificationContext } from '../../context/NotificationContext';
import { notificationApi } from '../../api/notificationApi';
import { ROUTES } from '../../constants/routes';

/**
 * Top Navbar Component
 *
 * Sticky header with global search, live in-app notification center dropdown,
 * system settings launcher, active user profile badge, and Logout button.
 */
const Navbar = ({ title = 'Payroll Management' }) => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const { showToastSuccess, showConfirmDialog } = useNotificationContext();

  const [notifications, setNotifications] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [unreadCount, setUnreadCount] = useState(0);

  const fetchNotifications = async () => {
    if (user?.id) {
      try {
        const unread = await notificationApi.getUnreadNotifications(user.id);
        setNotifications(unread);
        setUnreadCount(unread.length);
      } catch (e) {
        // quiet catch
      }
    }
  };

  useEffect(() => {
    fetchNotifications();
    const interval = setInterval(fetchNotifications, 15000);
    return () => clearInterval(interval);
  }, [user?.id]);

  const handleMarkAsRead = async (id) => {
    try {
      await notificationApi.markAsRead(id);
      setNotifications((prev) => prev.filter((n) => n.id !== id));
      setUnreadCount((prev) => Math.max(0, prev - 1));
      showToastSuccess('Notification marked as read.');
    } catch (e) {
      // quiet
    }
  };

  const handleLogout = async () => {
    const confirmed = await showConfirmDialog({
      title: 'Sign Out?',
      text: 'Are you sure you want to log out of your GlobalPay session?',
      confirmButtonText: 'Yes, Sign Out',
    });

    if (confirmed) {
      await logout();
      showToastSuccess('You have been logged out successfully.');
      navigate(ROUTES.LOGIN);
    }
  };

  return (
    <header className="h-20 border-b border-[#e1e2ed] bg-[#faf8ff]/80 backdrop-blur-md sticky top-0 z-30 px-8 flex items-center justify-between">
      {/* Page Title & Global Search Bar */}
      <div className="flex items-center gap-8 flex-1">
        <h2 className="font-heading font-bold text-2xl text-[#191b23] tracking-tight whitespace-nowrap">
          {title}
        </h2>
        <div className="relative max-w-md w-full hidden md:block">
          <HiOutlineMagnifyingGlass className="w-5 h-5 text-[#737686] absolute left-3.5 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search system analytics or employees..."
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7]/60 rounded-full pl-10 pr-4 py-2 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#2563eb] transition-all"
          />
        </div>
      </div>

      {/* Right Header Controls */}
      <div className="flex items-center gap-4">
        {/* Notification Bell with Dropdown */}
        <div className="relative">
          <button 
            aria-label="Notifications"
            onClick={() => setShowNotifications(!showNotifications)}
            className="w-10 h-10 rounded-full bg-[#f3f3fe] hover:bg-[#ededf9] border border-[#e1e2ed] flex items-center justify-center text-[#434655] relative transition-colors"
          >
            <HiOutlineBell className="w-5 h-5" />
            {unreadCount > 0 && (
              <span className="w-2.5 h-2.5 bg-[#ba1a1a] rounded-full absolute top-2 right-2 ring-2 ring-white"></span>
            )}
          </button>

          {/* Notifications Dropdown Panel */}
          {showNotifications && (
            <div className="absolute right-0 mt-3 w-80 bg-white border border-[#e1e2ed] rounded-2xl shadow-xl z-50 overflow-hidden">
              <div className="p-4 bg-[#f3f3fe] border-b border-[#e1e2ed] flex items-center justify-between">
                <span className="font-heading font-bold text-sm text-[#191b23]">Notifications</span>
                <span className="text-[10px] font-bold px-2 py-0.5 rounded-full bg-[#dbe1ff] text-[#004ac6]">
                  {unreadCount} Unread
                </span>
              </div>

              <div className="max-h-72 overflow-y-auto divide-y divide-[#e1e2ed]/60">
                {notifications.length === 0 ? (
                  <div className="p-6 text-center text-xs text-[#737686]">
                    <HiOutlineInformationCircle className="w-6 h-6 mx-auto mb-2 text-[#737686]/60" />
                    No new unread notifications.
                  </div>
                ) : (
                  notifications.map((n) => (
                    <div key={n.id} className="p-4 hover:bg-[#faf8ff] transition-colors flex items-start justify-between gap-3">
                      <div>
                        <h4 className="text-xs font-bold text-[#191b23]">{n.title}</h4>
                        <p className="text-[11px] text-[#737686] mt-0.5 leading-relaxed">{n.message}</p>
                      </div>
                      <button
                        onClick={() => handleMarkAsRead(n.id)}
                        title="Mark as read"
                        className="text-[#004ac6] hover:text-[#2563eb] p-1 rounded-md hover:bg-[#dbe1ff]"
                      >
                        <HiOutlineCheck className="w-4 h-4" />
                      </button>
                    </div>
                  ))
                )}
              </div>
            </div>
          )}
        </div>

        {/* Settings Gear */}
        <button 
          aria-label="Settings"
          onClick={() => navigate(ROUTES.SETTINGS)}
          className="w-10 h-10 rounded-full bg-[#f3f3fe] hover:bg-[#ededf9] border border-[#e1e2ed] flex items-center justify-center text-[#434655] transition-colors"
        >
          <HiOutlineCog6Tooth className="w-5 h-5" />
        </button>

        {/* User Profile Badge */}
        <div className="flex items-center gap-3 px-3 py-1.5 rounded-xl bg-[#f3f3fe]/80 border border-[#e1e2ed]">
          <img
            src={user?.avatar || `https://ui-avatars.com/api/?name=${encodeURIComponent(user?.name || 'User')}&background=004ac6&color=fff`}
            alt={user?.name || 'User'}
            className="w-9 h-9 rounded-full object-cover ring-2 ring-[#004ac6]/20"
          />
          <div className="hidden sm:block text-left pr-2">
            <h4 className="text-xs font-bold text-[#191b23] leading-tight">
              {user?.name || 'Admin User'}
            </h4>
            <p className="text-[10px] font-semibold text-[#737686]">
              {user?.role || 'SYSTEM ADMIN'}
            </p>
          </div>
        </div>

        {/* Explicit Logout Button */}
        <button
          onClick={handleLogout}
          title="Sign Out of GlobalPay"
          className="px-3.5 py-2 rounded-xl bg-[#fee2e2] hover:bg-[#fecaca] text-[#991b1b] font-semibold text-xs flex items-center gap-1.5 transition-all shadow-xs active:scale-[0.97]"
        >
          <HiOutlineArrowRightOnRectangle className="w-4 h-4" />
          <span className="hidden md:inline">Logout</span>
        </button>
      </div>
    </header>
  );
};

export default Navbar;
