import React, { useState } from 'react';
import { 
  HiOutlineCalendarDays, 
  HiOutlineCheckCircle, 
  HiOutlineClock, 
  HiOutlineArrowRightOnRectangle,
  HiOutlineArrowLeftOnRectangle,
  HiOutlineDocumentChartBar,
  HiOutlineTrash
} from 'react-icons/hi2';
import MetricCard from '../components/common/MetricCard';
import StatusBadge from '../components/common/StatusBadge';
import CheckInModal from '../components/attendance/CheckInModal';
import CheckOutModal from '../components/attendance/CheckOutModal';
import AttendanceReportModal from '../components/attendance/AttendanceReportModal';
import { useAttendance } from '../hooks/useAttendance';
import { useNotificationContext } from '../context/NotificationContext';

/**
 * Attendance Management Page Component
 *
 * Full Attendance CRUD, live check-in, check-out, date picker filtering,
 * overtime & late hours tracking, and periodic attendance report generation.
 */
const Attendance = () => {
  const { showToastSuccess, showToastError, showConfirmDialog } = useNotificationContext();
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10));
  
  const [isCheckInOpen, setIsCheckInOpen] = useState(false);
  const [isCheckOutOpen, setIsCheckOutOpen] = useState(false);
  const [isReportOpen, setIsReportOpen] = useState(false);

  const { 
    attendanceLogs, 
    totalRecords, 
    isLoading, 
    checkIn, 
    checkOut, 
    deleteAttendance 
  } = useAttendance(selectedDate);

  const presentCount = attendanceLogs.filter((l) => l.status === 'PRESENT').length;
  const lateCount = attendanceLogs.filter((l) => l.status === 'LATE').length;
  const totalHoursWorked = attendanceLogs.reduce((sum, l) => {
    const val = parseFloat(l.workHours) || 0;
    return sum + val;
  }, 0);

  const handleDelete = async (id, name) => {
    const confirmed = await showConfirmDialog({
      title: 'Delete Attendance Record?',
      text: `Remove attendance log for ${name}?`,
      confirmButtonText: 'Yes, Delete',
    });
    if (confirmed) {
      try {
        await deleteAttendance(id);
        showToastSuccess('Attendance record deleted.');
      } catch (err) {
        showToastError(err.response?.data?.message || 'Failed to delete record.');
      }
    }
  };

  return (
    <div className="space-y-6 pb-8">
      {/* Top Header & Action Buttons */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
            Attendance Management
          </h1>
          <p className="text-sm text-[#737686] mt-1">
            Track daily shifts, calculate late arrivals (after 08:30 AM), and overtime (beyond 8.0 hrs).
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-3">
          <button
            onClick={() => setIsReportOpen(true)}
            className="px-4 py-2.5 bg-white border border-[#c3c6d7] hover:bg-[#f3f3fe] text-[#191b23] text-xs font-bold rounded-xl flex items-center gap-2 shadow-xs transition-colors"
          >
            <HiOutlineDocumentChartBar className="w-4 h-4" />
            <span>Attendance Report</span>
          </button>
          <button
            onClick={() => setIsCheckInOpen(true)}
            className="px-4 py-2.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-bold rounded-xl flex items-center gap-2 shadow-md transition-all active:scale-[0.98]"
          >
            <HiOutlineArrowRightOnRectangle className="w-4 h-4" />
            <span>Record Check-In</span>
          </button>
          <button
            onClick={() => setIsCheckOutOpen(true)}
            className="px-4 py-2.5 bg-[#191b23] hover:bg-[#2e3039] text-white text-xs font-bold rounded-xl flex items-center gap-2 shadow-md transition-all active:scale-[0.98]"
          >
            <HiOutlineArrowLeftOnRectangle className="w-4 h-4" />
            <span>Record Check-Out</span>
          </button>
        </div>
      </div>

      {/* Top Metrics Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <MetricCard
          title="LOGGED ATTENDANCE TODAY"
          value={String(totalRecords)}
          badge="Shift Entries"
          badgeType="info"
          icon={HiOutlineCalendarDays}
        />
        <MetricCard
          title="ON TIME / PRESENT"
          value={String(presentCount)}
          badge={totalRecords > 0 ? `${Math.round((presentCount / totalRecords) * 100)}%` : '100%'}
          badgeType="success"
          icon={HiOutlineCheckCircle}
        />
        <MetricCard
          title="LATE ARRIVALS"
          value={String(lateCount)}
          badge="After 08:30 AM"
          badgeType={lateCount > 0 ? 'danger' : 'success'}
          icon={HiOutlineClock}
        />
      </div>

      {/* Date Filter & Daily Attendance Logs Table */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
        <div className="px-6 py-4 border-b border-[#e1e2ed] flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <h3 className="font-heading font-bold text-lg text-[#191b23]">
            Shift Logs for Date: {selectedDate}
          </h3>

          <div className="flex items-center gap-2">
            <span className="text-xs font-semibold text-[#737686]">Select Date:</span>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              className="bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3 py-1.5 text-xs text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                <th className="py-3.5 px-6">Employee</th>
                <th className="py-3.5 px-6">Employee No</th>
                <th className="py-3.5 px-6">Check In</th>
                <th className="py-3.5 px-6">Check Out</th>
                <th className="py-3.5 px-6">Working Hours</th>
                <th className="py-3.5 px-6">Late Hours</th>
                <th className="py-3.5 px-6">Overtime (OT)</th>
                <th className="py-3.5 px-6 text-center">Status</th>
                <th className="py-3.5 px-6 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
              {attendanceLogs.length === 0 ? (
                <tr>
                  <td colSpan="9" className="py-8 text-center text-xs text-[#737686]">
                    {isLoading ? 'Loading shift records...' : `No attendance logs found for ${selectedDate}. Use "Record Check-In" to log entry.`}
                  </td>
                </tr>
              ) : (
                attendanceLogs.map((log) => (
                  <tr key={log.id} className="hover:bg-[#faf8ff] transition-colors">
                    <td className="py-4 px-6 font-bold text-[#191b23]">{log.name}</td>
                    <td className="py-4 px-6 font-mono text-xs text-[#434655]">{log.employeeNumber}</td>
                    <td className="py-4 px-6 font-mono text-xs font-semibold text-[#004ac6]">{log.checkIn}</td>
                    <td className="py-4 px-6 font-mono text-xs font-semibold text-[#191b23]">{log.checkOut}</td>
                    <td className="py-4 px-6 text-xs font-semibold text-[#191b23]">{log.workHours}</td>
                    <td className="py-4 px-6 text-xs font-semibold text-[#ba1a1a]">{log.lateHours}</td>
                    <td className="py-4 px-6 text-xs font-semibold text-[#166534]">{log.overtimeHours}</td>
                    <td className="py-4 px-6 text-center">
                      <StatusBadge status={log.status} />
                    </td>
                    <td className="py-4 px-6 text-right">
                      <button
                        title="Delete Attendance Record"
                        onClick={() => handleDelete(log.id, log.name)}
                        className="text-[#737686] hover:text-[#ba1a1a] p-1 transition-colors"
                      >
                        <HiOutlineTrash className="w-4 h-4" />
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modals */}
      <CheckInModal
        isOpen={isCheckInOpen}
        onClose={() => setIsCheckInOpen(false)}
        onCheckIn={checkIn}
      />

      <CheckOutModal
        isOpen={isCheckOutOpen}
        onClose={() => setIsCheckOutOpen(false)}
        onCheckOut={checkOut}
      />

      <AttendanceReportModal
        isOpen={isReportOpen}
        onClose={() => setIsReportOpen(false)}
      />
    </div>
  );
};

export default Attendance;
