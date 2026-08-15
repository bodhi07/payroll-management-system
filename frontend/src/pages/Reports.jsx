import React, { useState, useEffect } from 'react';
import { 
  HiOutlineDocumentChartBar, 
  HiOutlineArrowDownTray, 
  HiOutlineCalendar,
  HiOutlineChartPie,
  HiOutlineShieldCheck
} from 'react-icons/hi2';
import MetricCard from '../components/common/MetricCard';
import StatusBadge from '../components/common/StatusBadge';
import { useReports } from '../hooks/useReports';
import { exportToCSV, printPayslip } from '../utils/exportUtils';
import { formatCurrency } from '../utils/formatters';
import { useNotificationContext } from '../context/NotificationContext';
import { departmentApi } from '../api/departmentApi';

/**
 * Reports Center Page Component
 *
 * Real analytical insights computed directly from Spring Boot backend.
 */
const Reports = () => {
  const { showToastSuccess } = useNotificationContext();
  
  const [activeSegment, setActiveSegment] = useState('Templates');
  const [department, setDepartment] = useState('All Departments');
  const [departments, setDepartments] = useState([]);
  const [fullyCompliant, setFullyCompliant] = useState(true);
  const [pendingReview, setPendingReview] = useState(false);

  useEffect(() => {
    departmentApi.getDepartments().then(setDepartments).catch(() => {});
  }, []);

  const { data: reportData, isLoading } = useReports({ department });

  const monthlyReport = reportData?.monthlyPayroll || {
    period: 'Current Period',
    employeesIncluded: 0,
    totalExpense: 0,
    avgNetPay: 0,
    departmentCount: 0,
  };

  const breakdown = reportData?.departmentBreakdown || [];
  const topContributors = reportData?.topContributors || [];

  const handleExportExcel = () => {
    exportToCSV(topContributors, `GlobalPay_Payroll_Report_${monthlyReport.period.replace(/\//g, '_')}`);
    showToastSuccess('Monthly Payroll Report exported to CSV.');
  };

  const handleExportPDF = () => {
    printPayslip();
  };

  return (
    <div className="space-y-6 pb-8">
      {/* Main Header & Segment Switcher */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
            Reports Center
          </h1>
          <p className="text-sm text-[#737686] mt-1">
            Real-time organizational analytics, compliance tracking, and payroll expenditure.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <div className="bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl p-1 flex items-center gap-1">
            {['Templates', 'Custom Reports', 'Scheduled'].map((tab) => (
              <button
                key={tab}
                onClick={() => setActiveSegment(tab)}
                className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-all ${
                  activeSegment === tab
                    ? 'bg-[#004ac6] text-white shadow-xs'
                    : 'text-[#434655] hover:text-[#191b23]'
                }`}
              >
                {tab}
              </button>
            ))}
          </div>

          <div className="relative">
            <span className="bg-white border border-[#c3c6d7] rounded-xl px-3 py-2 text-xs font-semibold text-[#191b23] flex items-center gap-2">
              <HiOutlineCalendar className="w-4 h-4 text-[#737686]" />
              <span>FY {new Date().getFullYear()} Active</span>
            </span>
          </div>
        </div>
      </div>

      {/* Main Layout Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Left Filter & Popular Templates Column */}
        <div className="lg:col-span-1 space-y-6">
          <div className="bg-white border border-[#e1e2ed] rounded-2xl p-5 shadow-xs space-y-4">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-[#191b23]">
              Filters
            </h3>

            <div>
              <label className="block text-xs font-medium text-[#737686] mb-1">
                Department
              </label>
              <select
                value={department}
                onChange={(e) => setDepartment(e.target.value)}
                className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3 py-2 text-xs font-semibold text-[#191b23] focus:outline-none"
              >
                <option value="All Departments">All Departments</option>
                {departments.map((d) => (
                  <option key={d.id} value={d.name}>{d.name}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-xs font-medium text-[#737686] mb-2">
                Statutory Compliance
              </label>
              <div className="space-y-2">
                <label className="flex items-center gap-2 text-xs font-medium text-[#434655] cursor-pointer">
                  <input
                    type="checkbox"
                    checked={fullyCompliant}
                    onChange={(e) => setFullyCompliant(e.target.checked)}
                    className="w-4 h-4 rounded border-[#c3c6d7] text-[#004ac6]"
                  />
                  <span>EPF / ETF Compliant</span>
                </label>
                <label className="flex items-center gap-2 text-xs font-medium text-[#434655] cursor-pointer">
                  <input
                    type="checkbox"
                    checked={pendingReview}
                    onChange={(e) => setPendingReview(e.target.checked)}
                    className="w-4 h-4 rounded border-[#c3c6d7] text-[#004ac6]"
                  />
                  <span>Tax (PAYE) Withheld</span>
                </label>
              </div>
            </div>
          </div>

          <div className="bg-white border border-[#e1e2ed] rounded-2xl p-5 shadow-xs space-y-3">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-[#191b23] mb-3">
              Popular Report Presets
            </h3>

            <div className="p-3 bg-[#faf8ff] border border-[#e1e2ed] rounded-xl flex items-center gap-3 hover:bg-[#ededf9] cursor-pointer transition-colors">
              <div className="w-9 h-9 rounded-xl bg-[#ffede6] text-[#943700] flex items-center justify-center">
                <HiOutlineChartPie className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-xs font-bold text-[#191b23]">Department Budgets</h4>
                <p className="text-[10px] text-[#737686]">Real-time Live Sync</p>
              </div>
            </div>

            <div className="p-3 bg-[#faf8ff] border border-[#e1e2ed] rounded-xl flex items-center gap-3 hover:bg-[#ededf9] cursor-pointer transition-colors">
              <div className="w-9 h-9 rounded-xl bg-[#dbe1ff] text-[#004ac6] flex items-center justify-center">
                <HiOutlineShieldCheck className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-xs font-bold text-[#191b23]">Tax Compliance (PAYE)</h4>
                <p className="text-[10px] text-[#737686]">Statutory 6% Deductions</p>
              </div>
            </div>
          </div>
        </div>

        {/* Right Main Report View Column */}
        <div className="lg:col-span-3 space-y-6">
          <div className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-4">
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 rounded-2xl bg-[#dbe1ff] text-[#004ac6] flex items-center justify-center">
                <HiOutlineDocumentChartBar className="w-6 h-6" />
              </div>
              <div>
                <h3 className="font-heading font-extrabold text-xl text-[#191b23]">
                  {monthlyReport.title}
                </h3>
                <p className="text-xs text-[#737686] mt-0.5">
                  Period: {monthlyReport.period} • {monthlyReport.employeesIncluded} Staff Included
                </p>
              </div>
            </div>

            <div className="flex items-center gap-3">
              <button
                onClick={handleExportExcel}
                className="px-4 py-2.5 bg-white border border-[#c3c6d7] hover:bg-[#f3f3fe] text-[#191b23] text-xs font-bold rounded-xl flex items-center gap-2 shadow-xs transition-colors"
              >
                <HiOutlineArrowDownTray className="w-4 h-4" />
                <span>Export CSV</span>
              </button>

              <button
                onClick={handleExportPDF}
                className="px-4 py-2.5 bg-[#191b23] hover:bg-[#2e3039] text-white text-xs font-bold rounded-xl flex items-center gap-2 shadow-md transition-all active:scale-[0.98]"
              >
                <HiOutlineDocumentChartBar className="w-4 h-4" />
                <span>Print PDF</span>
              </button>
            </div>
          </div>

          {/* 3 Metric Cards Row */}
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
            <MetricCard
              title="TOTAL PAYROLL EXPENSE"
              value={formatCurrency(monthlyReport.totalExpense)}
              subtext="📈 Gross salary expenditure"
            />
            <MetricCard
              title="AVERAGE NET PAY"
              value={formatCurrency(monthlyReport.avgNetPay)}
              subtext="📊 Per employee average"
            />
            <MetricCard
              title="ACTIVE DEPARTMENTS"
              value={`${monthlyReport.departmentCount} Depts.`}
            >
              <div className="flex items-end gap-1.5 h-6 pt-1">
                {[60, 100, 75, 40, 85, 50].map((h, i) => (
                  <div key={i} className="flex-1 bg-[#2563eb] rounded-xs" style={{ height: `${h}%` }}></div>
                ))}
              </div>
            </MetricCard>
          </div>

          {/* Expense Breakdown by Department Stacked Bar Chart */}
          <div className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs space-y-6">
            <div className="flex items-center justify-between border-b border-[#e1e2ed] pb-4">
              <h3 className="font-heading font-extrabold text-sm uppercase tracking-wider text-[#191b23]">
                Expense Breakdown by Department
              </h3>
              <div className="flex items-center gap-4 text-xs font-semibold">
                <div className="flex items-center gap-2">
                  <span className="w-3 h-3 rounded-full bg-[#004ac6]"></span>
                  <span className="text-[#434655]">Basic Salary</span>
                </div>
                <div className="flex items-center gap-2">
                  <span className="w-3 h-3 rounded-full bg-[#943700]"></span>
                  <span className="text-[#434655]">Taxes & Statutory</span>
                </div>
              </div>
            </div>

            <div className="space-y-5">
              {breakdown.map((item, idx) => {
                const grossPct = item.total > 0 ? (item.gross / item.total) * 100 : 80;
                const taxPct = item.total > 0 ? (item.taxesBenefits / item.total) * 100 : 20;

                return (
                  <div key={idx} className="space-y-1.5">
                    <div className="flex items-center justify-between text-xs font-semibold">
                      <span className="text-[#191b23]">{item.department}</span>
                      <span className="font-bold text-[#191b23]">{formatCurrency(item.total)}</span>
                    </div>

                    <div className="w-full bg-[#f3f3fe] h-3.5 rounded-full flex overflow-hidden">
                      <div
                        className="bg-[#004ac6] h-full"
                        style={{ width: `${grossPct}%` }}
                      ></div>
                      <div
                        className="bg-[#943700] h-full"
                        style={{ width: `${taxPct}%` }}
                      ></div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Top Contributors to Expense Table */}
          <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
            <div className="px-6 py-4 border-b border-[#e1e2ed] flex items-center justify-between">
              <h3 className="font-heading font-bold text-base text-[#191b23]">
                Staff Payroll Roster
              </h3>
              <span className="text-xs font-bold text-[#004ac6]">
                {topContributors.length} Records
              </span>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                    <th className="py-3.5 px-6">Employee Name</th>
                    <th className="py-3.5 px-6">Department</th>
                    <th className="py-3.5 px-6">Gross Pay</th>
                    <th className="py-3.5 px-6">Net Pay</th>
                    <th className="py-3.5 px-6 text-right">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
                  {topContributors.map((row) => (
                    <tr key={row.id} className="hover:bg-[#faf8ff] transition-colors">
                      <td className="py-4 px-6">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 rounded-full bg-[#dbe1ff] text-[#00174b] font-bold text-xs flex items-center justify-center">
                            {row.initials}
                          </div>
                          <span className="font-bold text-[#191b23]">{row.name}</span>
                        </div>
                      </td>
                      <td className="py-4 px-6 text-xs font-semibold text-[#434655]">
                        {row.department}
                      </td>
                      <td className="py-4 px-6 font-semibold text-[#191b23]">
                        {row.grossPayFormatted}
                      </td>
                      <td className="py-4 px-6 font-semibold text-[#004ac6]">
                        {row.netPayFormatted}
                      </td>
                      <td className="py-4 px-6 text-right">
                        <StatusBadge status={row.status} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Reports;
