import React, { useState, useEffect } from 'react';
import Modal from '../common/Modal';
import { employeeApi } from '../../api/employeeApi';
import { attendanceApi } from '../../api/attendanceApi';
import { useNotificationContext } from '../../context/NotificationContext';

/**
 * Attendance Report Modal Component
 */
const AttendanceReportModal = ({ isOpen, onClose }) => {
  const { showToastError } = useNotificationContext();
  const [employees, setEmployees] = useState([]);
  const [selectedEmployeeId, setSelectedEmployeeId] = useState('');
  const [startDate, setStartDate] = useState(new Date(new Date().getFullYear(), new Date().getMonth(), 1).toISOString().slice(0, 10));
  const [endDate, setEndDate] = useState(new Date().toISOString().slice(0, 10));
  const [report, setReport] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (isOpen) {
      employeeApi.getEmployees({ page: 1, pageSize: 100 }).then((res) => {
        setEmployees(res.data || []);
        if (res.data && res.data.length > 0) {
          setSelectedEmployeeId(res.data[0].id);
        }
      }).catch(() => {});
    }
  }, [isOpen]);

  const handleGenerateReport = async (e) => {
    e.preventDefault();
    if (!selectedEmployeeId) return;
    setIsLoading(true);
    try {
      const res = await attendanceApi.getAttendanceReport(selectedEmployeeId, startDate, endDate);
      setReport(res);
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to generate attendance report.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Generate Attendance Report">
      <div className="space-y-5">
        <form onSubmit={handleGenerateReport} className="space-y-4">
          <div>
            <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
              Select Employee
            </label>
            <select
              value={selectedEmployeeId}
              onChange={(e) => setSelectedEmployeeId(e.target.value)}
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2 text-sm text-[#191b23] focus:outline-none"
            >
              {employees.map((emp) => (
                <option key={emp.id} value={emp.id}>
                  {emp.name} ({emp.employeeId})
                </option>
              ))}
            </select>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
                Start Date
              </label>
              <input
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3 py-2 text-sm text-[#191b23] focus:outline-none"
              />
            </div>
            <div>
              <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
                End Date
              </label>
              <input
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3 py-2 text-sm text-[#191b23] focus:outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full py-2.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-bold rounded-xl shadow-md transition-all"
          >
            {isLoading ? 'Computing Summary...' : 'Generate Shift Report'}
          </button>
        </form>

        {report && (
          <div className="mt-4 p-4 bg-[#f3f3fe] border border-[#e1e2ed] rounded-2xl space-y-3 animate-fadeIn">
            <h4 className="font-heading font-extrabold text-sm text-[#191b23] border-b border-[#e1e2ed] pb-2">
              Report for: {report.employeeName} ({report.employeeNumber})
            </h4>
            <div className="grid grid-cols-2 gap-3 text-xs">
              <div className="p-2.5 bg-white rounded-xl border border-[#e1e2ed]">
                <span className="text-[#737686] font-semibold">Total Present Days</span>
                <p className="font-heading font-extrabold text-lg text-[#166534] mt-0.5">{report.totalPresentDays}</p>
              </div>
              <div className="p-2.5 bg-white rounded-xl border border-[#e1e2ed]">
                <span className="text-[#737686] font-semibold">Total Late Days</span>
                <p className="font-heading font-extrabold text-lg text-[#ba1a1a] mt-0.5">{report.totalLateDays}</p>
              </div>
              <div className="p-2.5 bg-white rounded-xl border border-[#e1e2ed]">
                <span className="text-[#737686] font-semibold">Total Work Hours</span>
                <p className="font-heading font-extrabold text-lg text-[#004ac6] mt-0.5">{report.totalWorkingHours} hrs</p>
              </div>
              <div className="p-2.5 bg-white rounded-xl border border-[#e1e2ed]">
                <span className="text-[#737686] font-semibold">Total Overtime (OT)</span>
                <p className="font-heading font-extrabold text-lg text-[#943700] mt-0.5">{report.totalOvertimeHours} hrs</p>
              </div>
            </div>
          </div>
        )}
      </div>
    </Modal>
  );
};

export default AttendanceReportModal;
