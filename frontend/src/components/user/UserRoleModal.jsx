import React, { useState } from 'react';
import Modal from '../common/Modal';
import { useNotificationContext } from '../../context/NotificationContext';

/**
 * Assign User Role Modal Component (Admin RBAC)
 */
const UserRoleModal = ({ isOpen, onClose, user, onAssignRole }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const [selectedRole, setSelectedRole] = useState('ROLE_EMPLOYEE');
  const [isSubmitting, setIsSubmitting] = useState(false);

  if (!user) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await onAssignRole(user.id, selectedRole);
      showToastSuccess(`Role ${selectedRole} assigned to user ${user.username}!`);
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to assign role.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`Manage Role for: ${user.username}`}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="p-3 bg-[#f3f3fe] border border-[#e1e2ed] rounded-xl text-xs space-y-1">
          <p className="font-bold text-[#191b23]">User: {user.username} ({user.email})</p>
          <p className="text-[#737686]">Current Roles: {user.roles?.join(', ')}</p>
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Select Security Role to Grant *
          </label>
          <select
            value={selectedRole}
            onChange={(e) => setSelectedRole(e.target.value)}
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          >
            <option value="ROLE_ADMIN">ROLE_ADMIN (Full Administrator Privilege)</option>
            <option value="ROLE_HR">ROLE_HR (Human Resources Manager)</option>
            <option value="ROLE_EMPLOYEE">ROLE_EMPLOYEE (Standard Self-Service Access)</option>
          </select>
        </div>

        <div className="flex items-center justify-end gap-3 pt-4 border-t border-[#e1e2ed]">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2.5 rounded-xl border border-[#c3c6d7] text-sm font-semibold text-[#434655] hover:bg-[#ededf9]"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={isSubmitting}
            className="px-5 py-2.5 rounded-xl bg-[#004ac6] hover:bg-[#2563eb] text-white text-sm font-semibold shadow-md disabled:opacity-50"
          >
            {isSubmitting ? 'Assigning...' : 'Save Role Assignment'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default UserRoleModal;
