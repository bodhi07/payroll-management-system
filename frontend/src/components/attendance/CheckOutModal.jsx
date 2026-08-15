import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import Modal from '../common/Modal';
import { useNotificationContext } from '../../context/NotificationContext';
import { employeeApi } from '../../api/employeeApi';

/**
 * Check-Out Modal Form Component
 *
 * Logs check-out timestamp and computes working hours and overtime.
 */
const CheckOutModal = ({ isOpen, onClose, onCheckOut }) => {
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
      await onCheckOut(data);
      showToastSuccess('Check-out recorded successfully! Shift hours updated.');
      reset();
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Check-out failed. Ensure employee has a valid check-in today.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Log Employee Check-Out">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Select Employee *
          </label>
          <select
            {...register('employeeId', { required: 'Employee is required' })}
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
          >
            {employees.map((emp) => (
              <option key={emp.id} value={emp.id}>
                {emp.name} ({emp.employeeId}) - {emp.department}
              </option>
            ))}
          </select>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Date *
            </label>
            <input
              type="date"
              {...register('date', { required: 'Date is required' })}
              defaultValue={new Date().toISOString().slice(0, 10)}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Check-Out Time *
            </label>
            <input
              type="time"
              step="1"
              {...register('checkOutTime', { required: 'Time is required' })}
              defaultValue={new Date().toTimeString().slice(0, 8)}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <p className="text-[11px] text-[#737686]">
          * Note: Standard shift is 8.0 hours. Hours worked beyond 8.0 hours will automatically compute as Overtime (OT).
        </p>

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
            {isSubmitting ? 'Recording...' : 'Record Check-Out'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default CheckOutModal;
