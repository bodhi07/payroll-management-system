import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import Modal from '../common/Modal';
import { useNotificationContext } from '../../context/NotificationContext';

/**
 * Add Department Modal Component
 */
const AddDepartmentModal = ({ isOpen, onClose, onAdd }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      await onAdd(data);
      showToastSuccess(`Department ${data.name} created successfully!`);
      reset();
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to create department. Code may already exist.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Create New Department">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Department Name *
          </label>
          <input
            type="text"
            {...register('name', { required: 'Department name is required' })}
            placeholder="e.g. Legal & Compliance"
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
          />
          {errors.name && <p className="text-xs text-[#ba1a1a] mt-1">{errors.name.message}</p>}
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Department Code *
          </label>
          <input
            type="text"
            {...register('code', { required: 'Code is required' })}
            placeholder="e.g. DEPT-LEG"
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none uppercase"
          />
          {errors.code && <p className="text-xs text-[#ba1a1a] mt-1">{errors.code.message}</p>}
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Description
          </label>
          <textarea
            rows="3"
            {...register('description')}
            placeholder="Corporate legal advisory and regulatory compliance management..."
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2 text-sm text-[#191b23] focus:outline-none"
          ></textarea>
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
            {isSubmitting ? 'Creating...' : 'Create Department'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default AddDepartmentModal;
