import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import Modal from '../common/Modal';
import { employeeApi } from '../../api/employeeApi';
import { useNotificationContext } from '../../context/NotificationContext';

/**
 * Apply Leave Modal Form Component
 */
const ApplyLeaveModal = ({ isOpen, onClose, onApply }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const { register, handleSubmit, reset } = useForm();
  const [employees, setEmployees] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (isOpen) {
      employeeApi.getEmployees({ page: 1, pageSize: 100 }).then((res) => {
        setEmployees(res.data || []);
      }).catch(() => {});
    }
  }, [isOpen]);

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      await onApply(data);
      showToastSuccess('Leave application submitted successfully for review!');
      reset();
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to submit leave application.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Apply For Employee Leave">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Select Employee *
          </label>
          <select
            {...register('employeeId', { required: 'Employee is required' })}
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          >
            {employees.map((emp) => (
              <option key={emp.id} value={emp.id}>
                {emp.name} ({emp.employeeId}) - {emp.department}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Leave Type *
          </label>
          <select
            {...register('leaveType', { required: 'Leave type is required' })}
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          >
            <option value="ANNUAL">Annual Leave (Quota: 14 Days)</option>
            <option value="CASUAL">Casual Leave (Quota: 7 Days)</option>
            <option value="MEDICAL">Medical Leave (Quota: 14 Days)</option>
          </select>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Start Date *
            </label>
            <input
              type="date"
              {...register('startDate', { required: 'Start date is required' })}
              defaultValue={new Date().toISOString().slice(0, 10)}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              End Date *
            </label>
            <input
              type="date"
              {...register('endDate', { required: 'End date is required' })}
              defaultValue={new Date(Date.now() + 86400000).toISOString().slice(0, 10)}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Reason / Justification
          </label>
          <textarea
            rows="3"
            {...register('reason')}
            placeholder="e.g. Annual family vacation or medical treatment..."
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
            {isSubmitting ? 'Submitting...' : 'Submit Application'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default ApplyLeaveModal;
