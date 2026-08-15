import React, { useState } from 'react';
import { 
  HiOutlineArrowDownTray, 
  HiOutlinePlay, 
  HiOutlineBanknotes, 
  HiOutlineAdjustmentsHorizontal, 
  HiOutlineScale,
  HiOutlineBuildingLibrary,
  HiOutlineTrash
} from 'react-icons/hi2';
import MetricCard from '../components/common/MetricCard';
import PayslipPreviewPanel from '../components/payroll/PayslipPreviewPanel';
import GeneratePayrollModal from '../components/payroll/GeneratePayrollModal';
import { usePayroll } from '../hooks/usePayroll';
import { exportToCSV } from '../utils/exportUtils';
import { formatCurrency } from '../utils/formatters';
import { useNotificationContext } from '../context/NotificationContext';
import StatusBadge from '../components/common/StatusBadge';

/**
 * Payroll Management Page Component
 *
 * Full Payroll CRUD, statutory calculations (EPF 8%/12%, ETF 3%, Tax),
 * month/year filtering, payslip inspector, and salary disbursement workflows.
 */
const Payroll = () => {
  const { showToastSuccess, showToastError, showConfirmDialog } = useNotificationContext();
  
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1);
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());
  const [isGenerateModalOpen, setIsGenerateModalOpen] = useState(false);
  const [selectedEmployeeId, setSelectedEmployeeId] = useState(null);

  const { 
    payrollData, 
    isLoading, 
    generatePayroll, 
    markAsPaid, 
    isMarkingPaid, 
    deletePayroll 
  } = usePayroll(selectedMonth, selectedYear);

  const metrics = payrollData?.metrics || {
    grossPay: 0,
    totalDeductions: 0,
    netPayroll: 0,
    taxWithholdings: 0,
  };

  const employees = payrollData?.employees || [];
  const selectedEmployee = employees.find((e) => e.id === selectedEmployeeId) || employees[0];

  const handleMarkPaid = async (payrollId) => {
    try {
      await markAsPaid(payrollId);
      showToastSuccess('Payroll entry marked as PAID. Disbursed successfully.');
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to update payment status.');
    }
  };

  const handleDelete = async (id, name) => {
    const confirmed = await showConfirmDialog({
      title: 'Delete Payroll Entry?',
      text: `Remove payroll calculation for ${name}?`,
      confirmButtonText: 'Yes, Delete',
    });
    if (confirmed) {
      try {
        await deletePayroll(id);
        showToastSuccess('Payroll record deleted.');
      } catch (err) {
        showToastError(err.response?.data?.message || 'Failed to delete payroll record.');
      }
    }
  };

  const handleExportRecords = () => {
    exportToCSV(employees, `GlobalPay_Payroll_Run_Month_${selectedMonth}_${selectedYear}`);
    showToastSuccess('Payroll records exported to CSV.');
  };

  return (
    <div className="space-y-6 pb-8">
      {/* Top Title & Header Action Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
            Payroll Management
          </h1>
          <p className="text-sm text-[#737686] mt-1">
            {payrollData?.period || `Cycle: Month ${String(selectedMonth).padStart(2, '0')}/${selectedYear}`}
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-3">
          {/* Month & Year Selectors */}
          <div className="flex items-center gap-2 bg-white border border-[#c3c6d7] rounded-xl px-3 py-1.5 shadow-xs">
            <select
              value={selectedMonth}
              onChange={(e) => setSelectedMonth(Number(e.target.value))}
              className="bg-transparent text-xs font-bold text-[#191b23] focus:outline-none"
            >
              {[
                { m: 1, n: 'Jan' }, { m: 2, n: 'Feb' }, { m: 3, n: 'Mar' },
                { m: 4, n: 'Apr' }, { m: 5, n: 'May' }, { m: 6, n: 'Jun' },
                { m: 7, n: 'Jul' }, { m: 8, n: 'Aug' }, { m: 9, n: 'Sep' },
                { m: 10, n: 'Oct' }, { m: 11, n: 'Nov' }, { m: 12, n: 'Dec' },
              ].map(({ m, n }) => (
                <option key={m} value={m}>{n}</option>
              ))}
            </select>
            <input
              type="number"
              value={selectedYear}
              onChange={(e) => setSelectedYear(Number(e.target.value))}
              className="w-16 bg-transparent text-xs font-bold text-[#191b23] focus:outline-none"
            />
          </div>

          <button
            onClick={handleExportRecords}
            className="px-4 py-2.5 bg-white border border-[#c3c6d7] hover:bg-[#f3f3fe] text-[#191b23] text-xs font-bold rounded-xl flex items-center gap-2 shadow-xs transition-colors"
          >
            <HiOutlineArrowDownTray className="w-4 h-4" />
            <span>Export CSV</span>
          </button>

          <button
            onClick={() => setIsGenerateModalOpen(true)}
            className="px-5 py-2.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-bold rounded-xl flex items-center gap-2 shadow-md transition-all active:scale-[0.98]"
          >
            <HiOutlinePlay className="w-4 h-4" />
            <span>Calculate Payroll</span>
          </button>
        </div>
      </div>

      {/* 4 Summary Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <MetricCard
          title="TOTAL GROSS PAY"
          value={formatCurrency(metrics.grossPay)}
          badge="Gross Total"
          badgeType="success"
          icon={HiOutlineBanknotes}
        />
        <MetricCard
          title="TOTAL DEDUCTIONS"
          value={formatCurrency(metrics.totalDeductions)}
          badge="EPF + Tax"
          badgeType="info"
          icon={HiOutlineAdjustmentsHorizontal}
        />
        <MetricCard
          title="NET PAYROLL OUTFLOW"
          value={formatCurrency(metrics.netPayroll)}
          badge="Net Payable"
          badgeType="info"
          icon={HiOutlineScale}
        />
        <MetricCard
          title="TAX WITHHOLDINGS"
          value={formatCurrency(metrics.taxWithholdings)}
          badge="PAYE Tax"
          badgeType="info"
          icon={HiOutlineBuildingLibrary}
        />
      </div>

      {/* Main Content Grid: 2/3 Employee Table + 1/3 Payslip Preview Panel */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Column: Employee Breakdown Table */}
        <div className="lg:col-span-2 bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden flex flex-col justify-between">
          <div>
            <div className="p-6 border-b border-[#e1e2ed] flex items-center justify-between">
              <h3 className="font-heading font-bold text-lg text-[#191b23]">
                Monthly Employee Salary Breakdown ({employees.length} Records)
              </h3>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                    <th className="py-4 px-6">Employee</th>
                    <th className="py-4 px-6">Basic Salary</th>
                    <th className="py-4 px-6">Gross Pay</th>
                    <th className="py-4 px-6">Deductions</th>
                    <th className="py-4 px-6">Net Payable</th>
                    <th className="py-4 px-6 text-center">Status</th>
                    <th className="py-4 px-6 text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
                  {employees.length === 0 ? (
                    <tr>
                      <td colSpan="7" className="py-8 text-center text-xs text-[#737686]">
                        {isLoading ? 'Loading payroll records...' : `No payroll records for ${selectedMonth}/${selectedYear}. Click "Calculate Payroll" to generate.`}
                      </td>
                    </tr>
                  ) : (
                    employees.map((emp) => {
                      const isSelected = selectedEmployee?.id === emp.id;
                      return (
                        <tr
                          key={emp.id}
                          onClick={() => setSelectedEmployeeId(emp.id)}
                          className={`cursor-pointer transition-colors ${
                            isSelected
                              ? 'bg-[#eff6ff] border-l-4 border-l-[#004ac6]'
                              : 'hover:bg-[#faf8ff]'
                          }`}
                        >
                          <td className="py-4 px-6">
                            <div className="flex items-center gap-3">
                              <div className="w-9 h-9 rounded-full bg-[#dbe1ff] text-[#00174b] font-bold text-xs flex items-center justify-center">
                                {emp.initials}
                              </div>
                              <div>
                                <p className="font-bold text-[#191b23] leading-tight">{emp.name}</p>
                                <p className="text-[10px] font-semibold tracking-wider text-[#737686] uppercase">
                                  {emp.designation} • {emp.department}
                                </p>
                              </div>
                            </div>
                          </td>
                          <td className="py-4 px-6 font-semibold text-[#191b23]">
                            {formatCurrency(emp.basicSalary || emp.baseSalary)}
                          </td>
                          <td className="py-4 px-6 font-semibold text-[#004ac6]">
                            {formatCurrency(emp.grossSalary)}
                          </td>
                          <td className="py-4 px-6 font-semibold text-[#ba1a1a]">
                            ({formatCurrency(Math.abs(emp.totalDeductions || emp.deductions))})
                          </td>
                          <td className="py-4 px-6 font-bold text-[#166534]">
                            {formatCurrency(emp.netPayable || emp.netSalary)}
                          </td>
                          <td className="py-4 px-6 text-center">
                            <StatusBadge status={emp.status} />
                          </td>
                          <td className="py-4 px-6 text-right">
                            <button
                              title="Delete Payroll Record"
                              onClick={(e) => {
                                e.stopPropagation();
                                handleDelete(emp.id, emp.name);
                              }}
                              className="text-[#737686] hover:text-[#ba1a1a] p-1 transition-colors"
                            >
                              <HiOutlineTrash className="w-4 h-4" />
                            </button>
                          </td>
                        </tr>
                      );
                    })
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* Right Column: Live Payslip Inspector Panel */}
        <div className="lg:col-span-1">
          <PayslipPreviewPanel
            employee={selectedEmployee}
            onMarkAsPaid={handleMarkPaid}
            isMarkingPaid={isMarkingPaid}
          />
        </div>
      </div>

      {/* Generate Payroll Modal */}
      <GeneratePayrollModal
        isOpen={isGenerateModalOpen}
        defaultMonth={selectedMonth}
        defaultYear={selectedYear}
        onClose={() => setIsGenerateModalOpen(false)}
        onGenerate={generatePayroll}
      />
    </div>
  );
};

export default Payroll;
