import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import Modal from '../common/Modal';
import { authApi } from '../../api/authApi';
import { useNotificationContext } from '../../context/NotificationContext';

/**
 * Add / Create New System User Modal Component (Member 06)
 */
const AddUserModal = ({ isOpen, onClose, onUserCreated }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      await authApi.register({
        username: data.username,
        email: data.email,
        password: data.password,
        roles: [data.role],
      });
      showToastSuccess(`User account "${data.username}" created successfully with role ${data.role}!`);
      reset();
      onClose();
      if (onUserCreated) onUserCreated();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to create user. Username or email may already exist.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Create New System User (Member 06)">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Username *
          </label>
          <input
            type="text"
            {...register('username', { required: 'Username is required', minLength: { value: 3, message: 'Min 3 chars' } })}
            placeholder="e.g. sarah.j"
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          />
          {errors.username && <p className="text-xs text-[#ba1a1a] mt-1">{errors.username.message}</p>}
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Email Address *
          </label>
          <input
            type="email"
            {...register('email', { required: 'Email is required' })}
            placeholder="sarah.j@company.com"
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          />
          {errors.email && <p className="text-xs text-[#ba1a1a] mt-1">{errors.email.message}</p>}
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Initial Password *
          </label>
          <input
            type="password"
            {...register('password', { required: 'Password is required', minLength: { value: 6, message: 'Min 6 chars' } })}
            placeholder="••••••••"
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          />
          {errors.password && <p className="text-xs text-[#ba1a1a] mt-1">{errors.password.message}</p>}
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Assign Security Role (RBAC) *
          </label>
          <select
            {...register('role')}
            defaultValue="ROLE_EMPLOYEE"
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          >
            <option value="ROLE_ADMIN">ROLE_ADMIN (Full System Administrator)</option>
            <option value="ROLE_HR">ROLE_HR (Human Resources Manager)</option>
            <option value="ROLE_EMPLOYEE">ROLE_EMPLOYEE (Standard Staff Self-Service)</option>
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
            {isSubmitting ? 'Creating User...' : 'Create Account'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default AddUserModal;
