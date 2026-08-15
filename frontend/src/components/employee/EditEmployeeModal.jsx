import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import Modal from '../common/Modal';
import { useNotificationContext } from '../../context/NotificationContext';
import { departmentApi } from '../../api/departmentApi';

/**
 * Edit Employee Modal Component
 */
const EditEmployeeModal = ({ isOpen, onClose, employee, onUpdate }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const { register, handleSubmit, reset, setValue, formState: { errors } } = useForm();
  const [departments, setDepartments] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (isOpen) {
      departmentApi.getDepartments().then(setDepartments).catch(() => {});
      if (employee) {
        setValue('employeeNumber', employee.employeeNumber || employee.employeeId);
        setValue('firstName', employee.firstName || employee.name?.split(' ')[0] || '');
        setValue('lastName', employee.lastName || employee.name?.split(' ').slice(1).join(' ') || '');
        setValue('email', employee.email || '');
        setValue('phone', employee.phone || '');
        setValue('nic', employee.nic || '');
        setValue('gender', employee.gender || 'MALE');
        setValue('departmentId', employee.departmentId || 1);
        setValue('designation', employee.designation || '');
        setValue('basicSalary', employee.basicSalary || employee.baseSalary || '');
        setValue('joinDate', employee.joinDate || '');
        setValue('status', employee.status || 'ACTIVE');
        setValue('address', employee.address || '');
      }
    }
  }, [isOpen, employee, setValue]);

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      await onUpdate(employee.id, data);
      showToastSuccess(`Employee ${data.firstName} ${data.lastName} updated successfully!`);
      reset();
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Error updating employee.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`Edit Employee: ${employee?.name || ''}`}>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Employee Number
            </label>
            <input
              type="text"
              {...register('employeeNumber', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              NIC / SSN
            </label>
            <input
              type="text"
              {...register('nic', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              First Name *
            </label>
            <input
              type="text"
              {...register('firstName', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Last Name *
            </label>
            <input
              type="text"
              {...register('lastName', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Email *
            </label>
            <input
              type="email"
              {...register('email', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Phone *
            </label>
            <input
              type="text"
              {...register('phone', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Department *
            </label>
            <select
              {...register('departmentId', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            >
              {departments.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name} ({d.code})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Designation *
            </label>
            <input
              type="text"
              {...register('designation', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Basic Salary ($ / LKR) *
            </label>
            <input
              type="number"
              step="0.01"
              {...register('basicSalary', { required: 'Required', min: 1 })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Status *
            </label>
            <select
              {...register('status', { required: 'Required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            >
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
              <option value="TERMINATED">TERMINATED</option>
            </select>
          </div>
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Residential Address
          </label>
          <input
            type="text"
            {...register('address')}
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
          />
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
            {isSubmitting ? 'Updating...' : 'Update Employee'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default EditEmployeeModal;
