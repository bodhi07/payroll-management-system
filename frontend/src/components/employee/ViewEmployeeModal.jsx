import React from 'react';
import Modal from '../common/Modal';
import StatusBadge from '../common/StatusBadge';
import { formatCurrency } from '../../utils/formatters';

/**
 * View Employee Details Profile Modal Component
 */
const ViewEmployeeModal = ({ isOpen, onClose, employee }) => {
  if (!employee) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Employee Profile Details">
      <div className="space-y-6">
        {/* Header Profile Summary */}
        <div className="flex items-center gap-4 p-4 bg-[#f3f3fe] rounded-2xl border border-[#e1e2ed]">
          <img
            src={employee.avatar || `https://ui-avatars.com/api/?name=${encodeURIComponent(employee.name || 'User')}&background=004ac6&color=fff`}
            alt={employee.name}
            className="w-16 h-16 rounded-2xl object-cover ring-2 ring-[#004ac6]/30"
          />
          <div>
            <h3 className="font-heading font-extrabold text-xl text-[#191b23]">{employee.name}</h3>
            <p className="text-xs font-medium text-[#737686]">{employee.designation} • {employee.department}</p>
            <div className="mt-2 flex items-center gap-2">
              <span className="text-[10px] font-mono font-bold bg-[#dbe1ff] text-[#004ac6] px-2 py-0.5 rounded-md">
                {employee.employeeId || employee.employeeNumber}
              </span>
              <StatusBadge status={employee.status} />
            </div>
          </div>
        </div>

        {/* Info Grid */}
        <div className="grid grid-cols-2 gap-4 text-xs">
          <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
            <p className="text-[#737686] font-bold uppercase tracking-wider">Email Address</p>
            <p className="font-semibold text-[#191b23] mt-1 break-all">{employee.email}</p>
          </div>

          <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
            <p className="text-[#737686] font-bold uppercase tracking-wider">Phone Number</p>
            <p className="font-semibold text-[#191b23] mt-1">{employee.phone || 'N/A'}</p>
          </div>

          <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
            <p className="text-[#737686] font-bold uppercase tracking-wider">NIC / Identity No</p>
            <p className="font-semibold text-[#191b23] mt-1">{employee.nic || 'N/A'}</p>
          </div>

          <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
            <p className="text-[#737686] font-bold uppercase tracking-wider">Gender</p>
            <p className="font-semibold text-[#191b23] mt-1">{employee.gender || 'N/A'}</p>
          </div>

          <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
            <p className="text-[#737686] font-bold uppercase tracking-wider">Basic Salary</p>
            <p className="font-bold text-[#004ac6] text-sm mt-1">{formatCurrency(employee.basicSalary || employee.baseSalary || 0)}</p>
          </div>

          <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
            <p className="text-[#737686] font-bold uppercase tracking-wider">Date Joined</p>
            <p className="font-semibold text-[#191b23] mt-1">{employee.joinDate || 'N/A'}</p>
          </div>
        </div>

        {/* Address */}
        <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed] text-xs">
          <p className="text-[#737686] font-bold uppercase tracking-wider">Residential Address</p>
          <p className="font-medium text-[#191b23] mt-1">{employee.address || 'Not provided'}</p>
        </div>

        <div className="flex justify-end pt-2">
          <button
            onClick={onClose}
            className="px-5 py-2.5 rounded-xl bg-[#191b23] hover:bg-[#2e3039] text-white text-xs font-bold transition-colors"
          >
            Close Profile
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default ViewEmployeeModal;
