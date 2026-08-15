import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import Modal from '../common/Modal';
import { employeeApi } from '../../api/employeeApi';
import { useNotificationContext } from '../../context/NotificationContext';
import { formatCurrency } from '../../utils/formatters';

/**
 * Generate Payroll Calculation Modal Component
 *
 * Calculates gross earnings, EPF (8%/12%), ETF (3%), Tax, and Net salary for Spring Boot POST /api/v1/payroll/generate.
 */
const GeneratePayrollModal = ({ isOpen, onClose, onGenerate, defaultMonth, defaultYear }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const { register, handleSubmit, reset, watch } = useForm();
  const [employees, setEmployees] = useState([]);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (isOpen) {
      employeeApi.getEmployees({ page: 1, pageSize: 100 }).then((res) => {
        const list = res.data || [];
        setEmployees(list);
        if (list.length > 0) {
          setSelectedEmployee(list[0]);
        }
      }).catch(() => {});
    }
  }, [isOpen]);

  const watchedEmployeeId = watch('employeeId');
  const watchedAllowance = Number(watch('allowance') || 0);
  const watchedBonus = Number(watch('bonus') || 0);

  useEffect(() => {
    if (watchedEmployeeId) {
      const emp = employees.find((e) => e.id === Number(watchedEmployeeId));
      if (emp) setSelectedEmployee(emp);
    }
  }, [watchedEmployeeId, employees]);

  const basicSalary = selectedEmployee ? Number(selectedEmployee.basicSalary || selectedEmployee.baseSalary || 0) : 0;
  const estimatedGross = basicSalary + watchedAllowance + watchedBonus;
  const estimatedEpfEmp = basicSalary * 0.08;
  const estimatedTax = estimatedGross * 0.06;
  const estimatedDeductions = estimatedEpfEmp + estimatedTax;
  const estimatedNet = estimatedGross - estimatedDeductions;

  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      await onGenerate(data);
      showToastSuccess(`Payroll for ${selectedEmployee?.name || 'Employee'} generated successfully!`);
      reset();
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to generate payroll. Ensure record does not already exist for this month/year.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Calculate & Generate Monthly Payroll">
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
                {emp.name} ({emp.employeeId}) - Base: {formatCurrency(emp.basicSalary || emp.baseSalary || 0)}
              </option>
            ))}
          </select>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Pay Month (1-12) *
            </label>
            <select
              {...register('month', { required: 'Month is required' })}
              defaultValue={defaultMonth || (new Date().getMonth() + 1)}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            >
              {[
                { m: 1, n: '01 - January' },
                { m: 2, n: '02 - February' },
                { m: 3, n: '03 - March' },
                { m: 4, n: '04 - April' },
                { m: 5, n: '05 - May' },
                { m: 6, n: '06 - June' },
                { m: 7, n: '07 - July' },
                { m: 8, n: '08 - August' },
                { m: 9, n: '09 - September' },
                { m: 10, n: '10 - October' },
                { m: 11, n: '11 - November' },
                { m: 12, n: '12 - December' },
              ].map(({ m, n }) => (
                <option key={m} value={m}>{n}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Pay Year *
            </label>
            <input
              type="number"
              {...register('year', { required: 'Year is required' })}
              defaultValue={defaultYear || new Date().getFullYear()}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Allowances ($ / LKR)
            </label>
            <input
              type="number"
              step="0.01"
              {...register('allowance')}
              defaultValue="0"
              placeholder="0.00"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Bonuses / Incentives ($ / LKR)
            </label>
            <input
              type="number"
              step="0.01"
              {...register('bonus')}
              defaultValue="0"
              placeholder="0.00"
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2.5 text-sm text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        {/* Live Calculation Preview Card */}
        <div className="p-4 bg-[#eff6ff] border border-[#bfdbfe] rounded-2xl space-y-2 text-xs">
          <p className="font-bold text-[#1e40af] uppercase tracking-wider">Live Statutory Estimate</p>
          <div className="grid grid-cols-2 gap-2 text-[#1e3a8a]">
            <div>Basic Salary: <span className="font-bold">{formatCurrency(basicSalary)}</span></div>
            <div>Estimated Gross: <span className="font-bold">{formatCurrency(estimatedGross)}</span></div>
            <div>EPF Employee (8%): <span className="font-bold">({formatCurrency(estimatedEpfEmp)})</span></div>
            <div>Tax (PAYE ~6%): <span className="font-bold">({formatCurrency(estimatedTax)})</span></div>
            <div className="col-span-2 pt-1 border-t border-[#bfdbfe] font-heading font-extrabold text-sm text-[#004ac6]">
              Estimated Net Salary: {formatCurrency(estimatedNet)}
            </div>
          </div>
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
            {isSubmitting ? 'Calculating...' : 'Generate & Save Payroll'}
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default GeneratePayrollModal;
