import React from 'react';
import { HiOutlinePrinter, HiOutlineCheckCircle, HiOutlinePaperAirplane } from 'react-icons/hi2';
import { printPayslip } from '../../utils/exportUtils';
import { formatCurrency } from '../../utils/formatters';
import StatusBadge from '../common/StatusBadge';

/**
 * Payslip Preview Panel Component
 *
 * Renders complete itemized payslip breakdown (Basic, Allowances, Gross, EPF 8%, Tax, Employer EPF 12%/ETF 3%, Net).
 */
const PayslipPreviewPanel = ({ employee, onMarkAsPaid, isMarkingPaid }) => {
  if (!employee) {
    return (
      <div className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs flex flex-col items-center justify-center text-center h-full text-xs text-[#737686]">
        Select an employee from the table to inspect itemized payslip.
      </div>
    );
  }

  const payslip = employee.payslip || {
    earnings: [{ label: 'Basic Salary', amount: employee.baseSalary || 0 }],
    grossEarnings: employee.grossSalary || employee.baseSalary || 0,
    deductions: [{ label: 'Total Deductions', amount: employee.deductions || 0 }],
    totalDeductions: employee.deductions || 0,
    netPayable: employee.netPayable || employee.baseSalary || 0,
    contributions: [],
  };

  return (
    <div id="printable-payslip" className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs flex flex-col justify-between h-full">
      <div>
        {/* Card Header & Print Action */}
        <div className="flex items-start justify-between pb-4 border-b border-[#e1e2ed]">
          <div>
            <div className="flex items-center gap-2">
              <h3 className="font-heading font-extrabold text-xl text-[#004ac6]">
                {employee.name}
              </h3>
              <StatusBadge status={employee.status} />
            </div>
            <p className="text-xs font-mono font-semibold text-[#737686] uppercase mt-0.5">
              EMP NO: {employee.employeeId}
            </p>
            <p className="text-xs font-medium text-[#434655] mt-1">
              Month {String(employee.month || 10).padStart(2, '0')}/{employee.year || 2023} Payslip
            </p>
          </div>
          <button
            onClick={printPayslip}
            aria-label="Print Payslip"
            title="Print Official Payslip"
            className="p-2 rounded-xl bg-[#f3f3fe] hover:bg-[#ededf9] text-[#434655] transition-colors"
          >
            <HiOutlinePrinter className="w-5 h-5" />
          </button>
        </div>

        {/* Earnings Breakdown Section */}
        <div className="mt-5 space-y-2.5">
          <div className="flex items-center gap-2 border-l-2 border-[#004ac6] pl-2 mb-3">
            <h4 className="text-xs font-bold uppercase tracking-wider text-[#191b23]">
              Earnings Breakdown
            </h4>
          </div>
          {payslip.earnings?.map((item, idx) => (
            <div key={idx} className="flex items-center justify-between text-xs text-[#434655]">
              <span>{item.label}</span>
              <span className="font-semibold text-[#191b23]">{formatCurrency(item.amount)}</span>
            </div>
          ))}
          <div className="flex items-center justify-between pt-2 text-xs font-bold text-[#004ac6]">
            <span>Gross Earnings</span>
            <span>{formatCurrency(payslip.grossEarnings)}</span>
          </div>
        </div>

        {/* Deductions Section */}
        <div className="mt-5 space-y-2.5 pt-4 border-t border-[#e1e2ed]/60">
          <div className="flex items-center gap-2 border-l-2 border-[#ba1a1a] pl-2 mb-3">
            <h4 className="text-xs font-bold uppercase tracking-wider text-[#191b23]">
              Employee Deductions
            </h4>
          </div>
          {payslip.deductions?.map((item, idx) => (
            <div key={idx} className="flex items-center justify-between text-xs text-[#434655]">
              <span>{item.label}</span>
              <span className="font-semibold text-[#ba1a1a]">({formatCurrency(Math.abs(item.amount))})</span>
            </div>
          ))}
          <div className="flex items-center justify-between pt-2 text-xs font-bold text-[#ba1a1a]">
            <span>Total Deductions</span>
            <span>({formatCurrency(Math.abs(payslip.totalDeductions))})</span>
          </div>
        </div>

        {/* Employer Statutory Contributions */}
        {payslip.contributions && payslip.contributions.length > 0 && (
          <div className="mt-5 space-y-2 pt-4 border-t border-[#e1e2ed]/60 text-xs">
            <div className="flex items-center gap-2 border-l-2 border-[#166534] pl-2 mb-2">
              <h4 className="text-xs font-bold uppercase tracking-wider text-[#166534]">
                Employer Contributions (Non-deductible)
              </h4>
            </div>
            {payslip.contributions.map((c, i) => (
              <div key={i} className="flex items-center justify-between text-[#737686]">
                <span>{c.label}</span>
                <span className="font-semibold text-[#166534]">{formatCurrency(c.amount)}</span>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* NET PAYABLE Banner & Action Button */}
      <div className="mt-6 space-y-3">
        <div className="bg-[#004ac6] text-white rounded-xl p-4 flex items-center justify-between shadow-md">
          <span className="text-xs font-bold uppercase tracking-wider text-blue-100">
            NET SALARY PAYABLE
          </span>
          <span className="font-heading font-extrabold text-2xl tracking-tight">
            {formatCurrency(payslip.netPayable)}
          </span>
        </div>

        {employee.status !== 'PAID' ? (
          <button
            onClick={() => onMarkAsPaid(employee.id || employee.payrollId)}
            disabled={isMarkingPaid}
            className="w-full py-3 px-4 bg-[#166534] hover:bg-[#15803d] text-white font-semibold text-xs rounded-xl flex items-center justify-center gap-2 shadow-sm transition-all disabled:opacity-50"
          >
            <HiOutlineCheckCircle className="w-4 h-4" />
            <span>{isMarkingPaid ? 'Updating...' : 'Mark As Paid (Disburse Salary)'}</span>
          </button>
        ) : (
          <div className="w-full py-2.5 bg-[#dcfce7] text-[#166534] font-bold text-xs rounded-xl text-center border border-[#bbf7d0]">
            Salary Disbursed & Paid on {employee.paidDate || 'Today'}
          </div>
        )}
      </div>
    </div>
  );
};

export default PayslipPreviewPanel;
