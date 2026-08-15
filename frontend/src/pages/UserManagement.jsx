import React, { useState, useEffect } from 'react';
import { 
  HiOutlineShieldCheck, 
  HiOutlineUserPlus, 
  HiOutlineUsers, 
  HiOutlineKey, 
  HiOutlineTrash,
  HiOutlineInformationCircle,
  HiOutlineLockClosed,
  HiOutlineSparkles,
  HiOutlineCheckBadge,
  HiOutlineArrowPath
} from 'react-icons/hi2';
import MetricCard from '../components/common/MetricCard';
import AddUserModal from '../components/user/AddUserModal';
import UserRoleModal from '../components/user/UserRoleModal';
import { userService } from '../services/userService';
import { useNotificationContext } from '../context/NotificationContext';

/**
 * Member 06: User Management & Role-Based Access Control (RBAC) Page
 *
 * Dedicated administrative page designed for Member 06.
 * Features full user lifecycle management, dynamic role assignment,
 * user search, role privileges matrix, and security auditing tools.
 */
const UserManagement = () => {
  const { showToastSuccess, showToastError, showConfirmDialog } = useNotificationContext();
  
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isAddUserOpen, setIsAddUserOpen] = useState(false);
  const [selectedUserForRole, setSelectedUserForRole] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  const fetchUsers = async () => {
    setIsLoading(true);
    try {
      const res = await userService.getAllUsers(0, 100);
      setUsers(res.data || []);
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to fetch user accounts. Requires ROLE_ADMIN access.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleAssignRole = async (userId, roleName) => {
    await userService.assignRole(userId, roleName);
    await fetchUsers();
  };

  const handleDeleteUser = async (id, username) => {
    const confirmed = await showConfirmDialog({
      title: 'Delete User Account?',
      text: `Are you sure you want to permanently delete user account "${username}"?`,
      confirmButtonText: 'Yes, Delete Account',
    });

    if (confirmed) {
      try {
        await userService.deleteUser(id);
        showToastSuccess(`User "${username}" deleted successfully.`);
        await fetchUsers();
      } catch (err) {
        showToastError(err.response?.data?.message || 'Failed to delete user.');
      }
    }
  };

  const filteredUsers = users.filter((u) => 
    u.username?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    u.email?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const adminCount = users.filter((u) => u.roles?.some((r) => r.includes('ADMIN'))).length;
  const hrCount = users.filter((u) => u.roles?.some((r) => r.includes('HR'))).length;
  const empCount = users.filter((u) => u.roles?.some((r) => r.includes('EMPLOYEE'))).length;

  return (
    <div className="space-y-8 pb-10">
      {/* Top Banner Guide - Explaining Easy System Use */}
      <div className="p-6 bg-gradient-to-r from-[#003ea8] to-[#004ac6] text-white rounded-2xl shadow-md relative overflow-hidden">
        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <span className="bg-white/20 text-white text-[10px] font-mono font-extrabold uppercase px-2.5 py-0.5 rounded-full border border-white/30">
                Member 06 Module
              </span>
              <span className="text-xs font-semibold text-blue-200">User Administration & RBAC</span>
            </div>
            <h1 className="font-heading font-extrabold text-2xl lg:text-3xl mt-1 tracking-tight">
              User Accounts & Security Privileges
            </h1>
            <p className="text-xs lg:text-sm text-blue-100 mt-1 max-w-2xl leading-relaxed">
              Manage system authentication, issue login credentials, and grant granular Role-Based Access Controls (<code className="bg-white/20 px-1 py-0.5 rounded">ROLE_ADMIN</code>, <code className="bg-white/20 px-1 py-0.5 rounded">ROLE_HR</code>, <code className="bg-white/20 px-1 py-0.5 rounded">ROLE_EMPLOYEE</code>).
            </p>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={fetchUsers}
              className="px-4 py-2.5 bg-white/15 hover:bg-white/25 text-white text-xs font-bold rounded-xl border border-white/20 flex items-center gap-2 transition-all shadow-xs"
            >
              <HiOutlineArrowPath className="w-4 h-4" />
              <span>Refresh</span>
            </button>
            <button
              onClick={() => setIsAddUserOpen(true)}
              className="px-5 py-2.5 bg-white text-[#004ac6] hover:bg-blue-50 text-xs font-extrabold rounded-xl flex items-center gap-2 transition-all shadow-md active:scale-[0.98]"
            >
              <HiOutlineUserPlus className="w-4 h-4" />
              <span>Create New User</span>
            </button>
          </div>
        </div>
      </div>

      {/* 4 Summary KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <MetricCard
          title="TOTAL USER ACCOUNTS"
          value={String(users.length)}
          badge="Database Accounts"
          badgeType="info"
          icon={HiOutlineUsers}
        />
        <MetricCard
          title="ADMINISTRATORS"
          value={String(adminCount)}
          badge="Full Access"
          badgeType="info"
          icon={HiOutlineShieldCheck}
        />
        <MetricCard
          title="HR MANAGERS"
          value={String(hrCount)}
          badge="HR Privileges"
          badgeType="success"
          icon={HiOutlineSparkles}
        />
        <MetricCard
          title="STANDARD EMPLOYEES"
          value={String(empCount)}
          badge="Self-Service"
          badgeType="info"
          icon={HiOutlineCheckBadge}
        />
      </div>

      {/* Main Table: User Accounts & Actions */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
        {/* Table Search Header */}
        <div className="p-6 border-b border-[#e1e2ed] flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h3 className="font-heading font-bold text-lg text-[#191b23]">
              Registered System Accounts ({filteredUsers.length})
            </h3>
            <p className="text-xs text-[#737686]">Click "Assign Role" to change privileges or permissions instantly.</p>
          </div>

          <div className="w-full sm:w-72">
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search by username or email..."
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2 text-xs text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
          </div>
        </div>

        {/* User Accounts Table */}
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                <th className="py-4 px-6">User Account</th>
                <th className="py-4 px-6">Email Address</th>
                <th className="py-4 px-6">Assigned RBAC Roles</th>
                <th className="py-4 px-6">Security Status</th>
                <th className="py-4 px-6 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
              {filteredUsers.length === 0 ? (
                <tr>
                  <td colSpan="5" className="py-8 text-center text-xs text-[#737686]">
                    {isLoading ? 'Loading system user accounts...' : 'No users found matching your search.'}
                  </td>
                </tr>
              ) : (
                filteredUsers.map((u) => (
                  <tr key={u.id} className="hover:bg-[#faf8ff] transition-colors">
                    {/* Username & Avatar */}
                    <td className="py-4 px-6">
                      <div className="flex items-center gap-3.5">
                        <img
                          src={`https://ui-avatars.com/api/?name=${encodeURIComponent(u.username)}&background=004ac6&color=fff`}
                          alt={u.username}
                          className="w-10 h-10 rounded-full object-cover ring-1 ring-[#e1e2ed]"
                        />
                        <div>
                          <p className="font-bold text-[#191b23] leading-tight">{u.username}</p>
                          <p className="text-[11px] font-mono text-[#737686]">User ID #{u.id}</p>
                        </div>
                      </div>
                    </td>

                    {/* Email */}
                    <td className="py-4 px-6 text-xs font-medium text-[#434655]">
                      {u.email}
                    </td>

                    {/* Roles Badges */}
                    <td className="py-4 px-6">
                      <div className="flex flex-wrap gap-1.5">
                        {u.roles?.map((r, i) => (
                          <span
                            key={i}
                            className={`text-[10px] font-mono font-bold px-2.5 py-1 rounded-lg ${
                              r.includes('ADMIN')
                                ? 'bg-[#dbe1ff] text-[#004ac6] border border-[#bfdbfe]'
                                : (r.includes('HR') ? 'bg-[#ffede6] text-[#943700] border border-[#fed7aa]' : 'bg-[#e2e8f0] text-[#334155] border border-[#cbd5e1]')
                            }`}
                          >
                            {r}
                          </span>
                        ))}
                      </div>
                    </td>

                    {/* Status */}
                    <td className="py-4 px-6">
                      <div className="flex items-center gap-1.5">
                        <span className="w-2 h-2 rounded-full bg-emerald-500"></span>
                        <span className="text-xs font-bold text-emerald-700">Active (BCrypt Secured)</span>
                      </div>
                    </td>

                    {/* Action Buttons */}
                    <td className="py-4 px-6 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          onClick={() => setSelectedUserForRole(u)}
                          className="px-3.5 py-1.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-bold rounded-xl shadow-xs flex items-center gap-1.5 transition-all"
                        >
                          <HiOutlineKey className="w-3.5 h-3.5" />
                          <span>Assign Role</span>
                        </button>

                        <button
                          title="Delete User Account"
                          onClick={() => handleDeleteUser(u.id, u.username)}
                          className="p-1.5 text-[#737686] hover:text-[#ba1a1a] hover:bg-[#fee2e2] rounded-xl transition-colors"
                        >
                          <HiOutlineTrash className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Role Privileges Reference Guide Card (For Super Easy Operation) */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs space-y-4">
        <div className="flex items-center gap-2 text-[#004ac6] font-bold text-sm">
          <HiOutlineInformationCircle className="w-5 h-5" />
          <span>Role-Based Access Control (RBAC) Permission Guide</span>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-xs">
          <div className="p-4 bg-[#f3f3fe] border border-[#dbe1ff] rounded-2xl space-y-2">
            <span className="font-mono font-extrabold text-[#004ac6] bg-white px-2 py-0.5 rounded-md border border-[#dbe1ff]">
              ROLE_ADMIN
            </span>
            <h4 className="font-heading font-extrabold text-sm text-[#191b23]">System Administrator</h4>
            <ul className="text-[#434655] space-y-1 list-disc pl-4 leading-relaxed">
              <li>Full CRUD access to all modules</li>
              <li>Create & manage users and assign security roles</li>
              <li>Inspect complete system audit logs & security telemetry</li>
              <li>Configure enterprise statutory rates & departments</li>
            </ul>
          </div>

          <div className="p-4 bg-[#faf8ff] border border-[#e1e2ed] rounded-2xl space-y-2">
            <span className="font-mono font-extrabold text-[#943700] bg-[#ffede6] px-2 py-0.5 rounded-md">
              ROLE_HR
            </span>
            <h4 className="font-heading font-extrabold text-sm text-[#191b23]">HR Specialist / Manager</h4>
            <ul className="text-[#434655] space-y-1 list-disc pl-4 leading-relaxed">
              <li>Full employee lifecycle directory management</li>
              <li>Calculate monthly payroll runs & disburse net pay</li>
              <li>Review, approve, and reject employee leave requests</li>
              <li>Manage shift attendance check-in / check-out logs</li>
            </ul>
          </div>

          <div className="p-4 bg-[#faf8ff] border border-[#e1e2ed] rounded-2xl space-y-2">
            <span className="font-mono font-extrabold text-[#334155] bg-[#e2e8f0] px-2 py-0.5 rounded-md">
              ROLE_EMPLOYEE
            </span>
            <h4 className="font-heading font-extrabold text-sm text-[#191b23]">Standard Staff User</h4>
            <ul className="text-[#434655] space-y-1 list-disc pl-4 leading-relaxed">
              <li>Self-service attendance check-in & check-out</li>
              <li>Submit time-off and leave applications</li>
              <li>View and print monthly salary payslips</li>
              <li>Inspect personal annual leave quota balance</li>
            </ul>
          </div>
        </div>
      </div>

      {/* Modals */}
      <AddUserModal
        isOpen={isAddUserOpen}
        onClose={() => setIsAddUserOpen(false)}
        onUserCreated={fetchUsers}
      />

      <UserRoleModal
        isOpen={!!selectedUserForRole}
        user={selectedUserForRole}
        onClose={() => setSelectedUserForRole(null)}
        onAssignRole={handleAssignRole}
      />
    </div>
  );
};

export default UserManagement;
