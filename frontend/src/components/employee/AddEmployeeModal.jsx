import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import Modal from '../common/Modal';
import { useNotificationContext } from '../../context/NotificationContext';
import { departmentApi } from '../../api/departmentApi';

/**
 * Add Employee Modal Form Component
 *
 * Captures all Jakarta validated fields for Spring Boot POST /api/v1/employees.
 */
const AddEmployeeModal = ({ isOpen, onClose, onAdd }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [departments, setDepartments] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (isOpen) {
      departmentApi.getDepartments().then(setDepartments).catch(() => {});
    }
  }, [isOpen]);

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      await onAdd(data);
      showToastSuccess(`Employee ${data.firstName} ${data.lastName} created successfully!`);
      reset();
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Error creating employee.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Add New Employee">
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        {/* Employee Number & NIC */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Employee Number *
            </label>
            <input
              type="text"
              {...register('employeeNumber', { required: 'Employee number is required' })}
              defaultValue={`EMP-${new Date().getFullYear()}-${Math.floor(100 + Math.random() * 900)}`}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.employeeNumber && <p className="text-xs text-[#ba1a1a] mt-1">{errors.employeeNumber.message}</p>}
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              NIC / SSN *
            </label>
            <input
              type="text"
              {...register('nic', { required: 'NIC is required' })}
              placeholder="e.g. 200012345678"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.nic && <p className="text-xs text-[#ba1a1a] mt-1">{errors.nic.message}</p>}
          </div>
        </div>

        {/* First & Last Name */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              First Name *
            </label>
            <input
              type="text"
              {...register('firstName', { required: 'First name is required' })}
              placeholder="e.g. Kamal"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.firstName && <p className="text-xs text-[#ba1a1a] mt-1">{errors.firstName.message}</p>}
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Last Name *
            </label>
            <input
              type="text"
              {...register('lastName', { required: 'Last name is required' })}
              placeholder="e.g. Silva"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.lastName && <p className="text-xs text-[#ba1a1a] mt-1">{errors.lastName.message}</p>}
          </div>
        </div>

        {/* Email & Phone */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Email Address *
            </label>
            <input
              type="email"
              {...register('email', { required: 'Email is required' })}
              placeholder="kamal.s@payroll.com"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.email && <p className="text-xs text-[#ba1a1a] mt-1">{errors.email.message}</p>}
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Phone Number *
            </label>
            <input
              type="text"
              {...register('phone', { required: 'Phone is required' })}
              placeholder="+94771234567"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.phone && <p className="text-xs text-[#ba1a1a] mt-1">{errors.phone.message}</p>}
          </div>
        </div>

        {/* Gender & Department */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Gender *
            </label>
            <select
              {...register('gender', { required: 'Gender is required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            >
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Department *
            </label>
            <select
              {...register('departmentId', { required: 'Department is required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            >
              {departments.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name} ({d.code})
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Designation & Basic Salary */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Designation *
            </label>
            <input
              type="text"
              {...register('designation', { required: 'Designation is required' })}
              placeholder="e.g. Software Engineer"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.designation && <p className="text-xs text-[#ba1a1a] mt-1">{errors.designation.message}</p>}
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Basic Salary (LKR / USD) *
            </label>
            <input
              type="number"
              step="0.01"
              {...register('basicSalary', { required: 'Basic salary is required', min: 1 })}
              placeholder="150000.00"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
            {errors.basicSalary && <p className="text-xs text-[#ba1a1a] mt-1">{errors.basicSalary.message}</p>}
          </div>
        </div>

        {/* Join Date & Status */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Join Date *
            </label>
            <input
              type="date"
              {...register('joinDate', { required: 'Join date is required' })}
              defaultValue={new Date().toISOString().slice(0, 10)}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Status *
            </label>
            <select
              {...register('status', { required: 'Status is required' })}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
            >
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
              <option value="TERMINATED">TERMINATED</option>
            </select>
          </div>
        </div>

        {/* Address */}
        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Residential Address
          </label>
          <input
            type="text"
            {...register('address')}
            placeholder="123 Galle Road, Colombo"
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20"
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
            {isSubmitting ? 'Saving...' : 'Save Employee'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default AddEmployeeModal;
