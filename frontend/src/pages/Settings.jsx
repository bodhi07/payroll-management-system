import React, { useState, useEffect } from 'react';
import { 
  HiOutlineShieldCheck, 
  HiOutlineUsers, 
  HiOutlineTrash, 
  HiOutlineKey,
  HiOutlineUserGroup
} from 'react-icons/hi2';
import UserRoleModal from '../components/user/UserRoleModal';
import { userService } from '../services/userService';
import { useNotificationContext } from '../context/NotificationContext';

/**
 * Application Settings & User RBAC Page Component
 */
const Settings = () => {
  const { showToastSuccess, showToastError, showConfirmDialog } = useNotificationContext();
  
  const [users, setUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedUserForRole, setSelectedUserForRole] = useState(null);

  const fetchUsers = async () => {
    setIsLoading(true);
    try {
      const res = await userService.getAllUsers(0, 50);
      setUsers(res.data || []);
    } catch (err) {
      // quiet catch if unauthorized or employee
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
      text: `Are you sure you want to permanently delete user "${username}"?`,
      confirmButtonText: 'Yes, Delete',
    });

    if (confirmed) {
      try {
        await userService.deleteUser(id);
        showToastSuccess(`User ${username} deleted successfully.`);
        await fetchUsers();
      } catch (err) {
        showToastError(err.response?.data?.message || 'Failed to delete user.');
      }
    }
  };

  return (
    <div className="space-y-8 pb-8 max-w-5xl">
      <div>
        <h1 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
          System Administration & RBAC
        </h1>
        <p className="text-sm text-[#737686] mt-1">
          Manage system users, grant security privileges (ROLE_ADMIN, ROLE_HR, ROLE_EMPLOYEE), and configure security parameters.
        </p>
      </div>

      {/* User Management & Role Assignment Table */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
        <div className="p-6 border-b border-[#e1e2ed] flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-[#dbe1ff] text-[#004ac6] flex items-center justify-center">
              <HiOutlineUserGroup className="w-5 h-5" />
            </div>
            <div>
              <h3 className="font-heading font-bold text-base text-[#191b23]">Registered User Accounts (RBAC)</h3>
              <p className="text-xs text-[#737686]">Member Module 06: Role-Based Access Control</p>
            </div>
          </div>
          <button
            onClick={fetchUsers}
            className="px-3 py-1.5 bg-[#f3f3fe] hover:bg-[#ededf9] text-[#004ac6] text-xs font-bold rounded-xl transition-colors"
          >
            Refresh Users
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                <th className="py-3.5 px-6">Username</th>
                <th className="py-3.5 px-6">Email Address</th>
                <th className="py-3.5 px-6">Assigned Roles</th>
                <th className="py-3.5 px-6">Account Status</th>
                <th className="py-3.5 px-6 text-right">Role Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
              {users.length === 0 ? (
                <tr>
                  <td colSpan="5" className="py-8 text-center text-xs text-[#737686]">
                    {isLoading ? 'Loading registered users...' : 'No users found or insufficient permissions (Requires ROLE_ADMIN).'}
                  </td>
                </tr>
              ) : (
                users.map((u) => (
                  <tr key={u.id} className="hover:bg-[#faf8ff] transition-colors">
                    <td className="py-4 px-6 font-bold text-[#191b23]">{u.username}</td>
                    <td className="py-4 px-6 text-xs text-[#434655]">{u.email}</td>
                    <td className="py-4 px-6">
                      <div className="flex flex-wrap gap-1">
                        {u.roles?.map((r, i) => (
                          <span
                            key={i}
                            className={`text-[10px] font-mono font-bold px-2 py-0.5 rounded-md ${
                              r.includes('ADMIN')
                                ? 'bg-[#dbe1ff] text-[#004ac6]'
                                : (r.includes('HR') ? 'bg-[#ffede6] text-[#943700]' : 'bg-[#e2e8f0] text-[#334155]')
                            }`}
                          >
                            {r}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td className="py-4 px-6">
                      <span className="inline-block w-2 h-2 rounded-full bg-emerald-500 mr-2"></span>
                      <span className="text-xs font-semibold text-emerald-700">Active</span>
                    </td>
                    <td className="py-4 px-6 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button
                          title="Assign Security Role"
                          onClick={() => setSelectedUserForRole(u)}
                          className="px-3 py-1 bg-[#dbe1ff] hover:bg-[#c2d0ff] text-[#004ac6] text-xs font-bold rounded-lg flex items-center gap-1 transition-colors"
                        >
                          <HiOutlineKey className="w-3.5 h-3.5" />
                          <span>Assign Role</span>
                        </button>
                        <button
                          title="Delete User"
                          onClick={() => handleDeleteUser(u.id, u.username)}
                          className="p-1 text-[#737686] hover:text-[#ba1a1a] transition-colors"
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

      {/* Security & System Configuration Card */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs space-y-6">
        <div className="flex items-center gap-4 pb-4 border-b border-[#e1e2ed]">
          <div className="w-10 h-10 rounded-xl bg-[#dbe1ff] text-[#004ac6] flex items-center justify-center">
            <HiOutlineShieldCheck className="w-5 h-5" />
          </div>
          <div>
            <h3 className="font-heading font-bold text-base text-[#191b23]">Authentication & Spring Boot Connectivity</h3>
            <p className="text-xs text-[#737686]">Stateless JWT Token Session & API Configuration</p>
          </div>
        </div>

        <div className="space-y-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Spring Boot API Base URL
            </label>
            <input
              type="text"
              readOnly
              defaultValue="http://localhost:8080/api/v1"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] font-mono focus:outline-none"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
                Access Token Expiration (Milliseconds)
              </label>
              <input
                type="text"
                readOnly
                defaultValue="86400000 (24 Hours)"
                className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] font-mono focus:outline-none"
              />
            </div>
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
                Refresh Token Expiration (Milliseconds)
              </label>
              <input
                type="text"
                readOnly
                defaultValue="604800000 (7 Days)"
                className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] font-mono focus:outline-none"
              />
            </div>
          </div>
        </div>
      </div>

      {/* User Role Modal */}
      <UserRoleModal
        isOpen={!!selectedUserForRole}
        user={selectedUserForRole}
        onClose={() => setSelectedUserForRole(null)}
        onAssignRole={handleAssignRole}
      />
    </div>
  );
};

export default Settings;
