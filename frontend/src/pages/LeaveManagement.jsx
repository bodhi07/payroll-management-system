import React, { useState, useEffect } from 'react';
import { 
  HiOutlineCheck, 
  HiOutlineXMark, 
  HiOutlineDocumentCheck, 
  HiOutlinePlusCircle,
  HiOutlineCalendar,
  HiOutlineSparkles
} from 'react-icons/hi2';
import MetricCard from '../components/common/MetricCard';
import StatusBadge from '../components/common/StatusBadge';
import ApplyLeaveModal from '../components/leave/ApplyLeaveModal';
import ReviewLeaveModal from '../components/leave/ReviewLeaveModal';
import { useLeave } from '../hooks/useLeave';
import { employeeApi } from '../api/employeeApi';
import { leaveApi } from '../api/leaveApi';
import { useNotificationContext } from '../context/NotificationContext';

/**
 * Leave Management Page Component
 *
 * Full Leave CRUD, status tabs (Pending, Approved, Rejected),
 * application submission, HR approval/rejection review workflow, and quota balances.
 */
const LeaveManagement = () => {
  const [activeStatus, setActiveStatus] = useState('PENDING');
  const [isApplyOpen, setIsApplyOpen] = useState(false);
  const [selectedRequestForReview, setSelectedRequestForReview] = useState(null);

  const [employees, setEmployees] = useState([]);
  const [selectedEmployeeForBalance, setSelectedEmployeeForBalance] = useState('');
  const [leaveBalance, setLeaveBalance] = useState(null);

  const { 
    leaveRequests, 
    totalRecords, 
    isLoading, 
    applyLeave, 
    approveLeave, 
    rejectLeave 
  } = useLeave(activeStatus);

  useEffect(() => {
    employeeApi.getEmployees({ page: 1, pageSize: 100 }).then((res) => {
      const list = res.data || [];
      setEmployees(list);
      if (list.length > 0) {
        setSelectedEmployeeForBalance(list[0].id);
      }
    }).catch(() => {});
  }, []);

  useEffect(() => {
    if (selectedEmployeeForBalance) {
      leaveApi.getLeaveBalance(selectedEmployeeForBalance).then(setLeaveBalance).catch(() => {});
    }
  }, [selectedEmployeeForBalance]);

  const handleReviewAction = async (id, decision, actionReason) => {
    if (decision === 'APPROVE') {
      await approveLeave({ id, actionReason });
    } else {
      await rejectLeave({ id, actionReason });
    }
    // Refresh balance if applicable
    if (selectedEmployeeForBalance) {
      leaveApi.getLeaveBalance(selectedEmployeeForBalance).then(setLeaveBalance).catch(() => {});
    }
  };

  return (
    <div className="space-y-6 pb-8">
      {/* Top Header & Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
            Leave Management
          </h1>
          <p className="text-sm text-[#737686] mt-1">
            Manage employee time-off requests, review applications, and maintain leave allowances.
          </p>
        </div>

        <button
          onClick={() => setIsApplyOpen(true)}
          className="px-5 py-2.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-bold rounded-xl flex items-center gap-2 shadow-md transition-all active:scale-[0.98]"
        >
          <HiOutlinePlusCircle className="w-4 h-4" />
          <span>Apply For Leave</span>
        </button>
      </div>

      {/* Summary Metrics Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <MetricCard
          title="ACTIVE TAB REQUESTS"
          value={String(totalRecords)}
          badge={`Status: ${activeStatus}`}
          badgeType="info"
          icon={HiOutlineDocumentCheck}
        />
        <MetricCard
          title="ANNUAL ALLOWANCE"
          value="14 Days"
          badge="Per Year"
          badgeType="success"
          icon={HiOutlineCalendar}
        />
        <MetricCard
          title="CASUAL / MEDICAL"
          value="7 / 14 Days"
          subtext="Standard Enterprise Quotas"
          icon={HiOutlineSparkles}
        />
      </div>

      {/* Live Employee Leave Balance Inspector */}
      <div className="p-5 bg-white border border-[#e1e2ed] rounded-2xl shadow-xs space-y-3">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-[#e1e2ed] pb-3">
          <h3 className="font-heading font-bold text-sm text-[#191b23]">
            Inspect Employee Leave Balance Quota ({new Date().getFullYear()})
          </h3>
          <div className="flex items-center gap-2">
            <span className="text-xs font-semibold text-[#737686]">Select Staff:</span>
            <select
              value={selectedEmployeeForBalance}
              onChange={(e) => setSelectedEmployeeForBalance(e.target.value)}
              className="bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3 py-1.5 text-xs text-[#191b23] focus:outline-none font-semibold"
            >
              {employees.map((emp) => (
                <option key={emp.id} value={emp.id}>
                  {emp.name} ({emp.employeeId})
                </option>
              ))}
            </select>
          </div>
        </div>

        {leaveBalance && (
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 pt-1 text-xs">
            <div className="p-3 bg-[#f3f3fe] rounded-xl border border-[#e1e2ed]">
              <span className="text-[#737686] font-bold uppercase tracking-wider">Annual Leave</span>
              <p className="font-heading font-extrabold text-xl text-[#004ac6] mt-1">
                {leaveBalance.remainingAnnualLeave} / {leaveBalance.totalAnnualLeave} Days Left
              </p>
            </div>
            <div className="p-3 bg-[#f3f3fe] rounded-xl border border-[#e1e2ed]">
              <span className="text-[#737686] font-bold uppercase tracking-wider">Casual Leave</span>
              <p className="font-heading font-extrabold text-xl text-[#004ac6] mt-1">
                {leaveBalance.remainingCasualLeave} / {leaveBalance.totalCasualLeave} Days Left
              </p>
            </div>
            <div className="p-3 bg-[#f3f3fe] rounded-xl border border-[#e1e2ed]">
              <span className="text-[#737686] font-bold uppercase tracking-wider">Medical Leave</span>
              <p className="font-heading font-extrabold text-xl text-[#004ac6] mt-1">
                {leaveBalance.remainingMedicalLeave} / {leaveBalance.totalMedicalLeave} Days Left
              </p>
            </div>
          </div>
        )}
      </div>

      {/* Leave Requests Table with Status Switcher */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
        {/* Filter Tabs */}
        <div className="px-6 py-4 border-b border-[#e1e2ed] flex items-center justify-between">
          <div className="flex items-center gap-3">
            {['PENDING', 'APPROVED', 'REJECTED'].map((st) => (
              <button
                key={st}
                onClick={() => setActiveStatus(st)}
                className={`px-3.5 py-1.5 rounded-xl text-xs font-bold transition-all ${
                  activeStatus === st
                    ? 'bg-[#004ac6] text-white shadow-xs'
                    : 'bg-[#f3f3fe] text-[#434655] hover:bg-[#ededf9]'
                }`}
              >
                {st} Requests
              </button>
            ))}
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                <th className="py-3.5 px-6">Employee</th>
                <th className="py-3.5 px-6">Leave Type</th>
                <th className="py-3.5 px-6">Duration</th>
                <th className="py-3.5 px-6">Dates</th>
                <th className="py-3.5 px-6">Reason</th>
                <th className="py-3.5 px-6">Status</th>
                <th className="py-3.5 px-6 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
              {leaveRequests.length === 0 ? (
                <tr>
                  <td colSpan="7" className="py-8 text-center text-xs text-[#737686]">
                    {isLoading ? 'Loading leave applications...' : `No ${activeStatus.toLowerCase()} leave requests found.`}
                  </td>
                </tr>
              ) : (
                leaveRequests.map((req) => (
                  <tr key={req.id} className="hover:bg-[#faf8ff] transition-colors">
                    <td className="py-4 px-6">
                      <p className="font-bold text-[#191b23]">{req.employeeName}</p>
                      <p className="text-xs text-[#737686]">{req.department}</p>
                    </td>
                    <td className="py-4 px-6 font-semibold text-[#004ac6]">{req.leaveType}</td>
                    <td className="py-4 px-6 text-xs font-bold text-[#191b23]">{req.durationDays || req.totalDays} Days</td>
                    <td className="py-4 px-6 text-xs text-[#434655]">{req.startDate} to {req.endDate}</td>
                    <td className="py-4 px-6 text-xs text-[#737686] max-w-xs truncate">{req.reason || 'N/A'}</td>
                    <td className="py-4 px-6">
                      <StatusBadge status={req.status} />
                    </td>
                    <td className="py-4 px-6 text-right">
                      {req.status === 'PENDING' ? (
                        <button
                          onClick={() => setSelectedRequestForReview(req)}
                          className="px-3.5 py-1.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-bold rounded-xl shadow-xs transition-colors"
                        >
                          Review & Decide
                        </button>
                      ) : (
                        <span className="text-[11px] text-[#737686] font-medium">
                          {req.actionReason || `Processed by ${req.approvedBy || 'Admin'}`}
                        </span>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modals */}
      <ApplyLeaveModal
        isOpen={isApplyOpen}
        onClose={() => setIsApplyOpen(false)}
        onApply={applyLeave}
      />

      <ReviewLeaveModal
        isOpen={!!selectedRequestForReview}
        request={selectedRequestForReview}
        onClose={() => setSelectedRequestForReview(null)}
        onAction={handleReviewAction}
      />
    </div>
  );
};

export default LeaveManagement;
