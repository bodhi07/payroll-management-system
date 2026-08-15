import React, { useState, useEffect } from 'react';
import { 
  HiOutlineShieldCheck, 
  HiOutlineUserPlus, 
  HiOutlineBanknotes, 
  HiOutlineCalendarDays, 
  HiOutlineClock,
  HiOutlineArrowDownTray,
  HiOutlineArrowPath,
  HiOutlineMagnifyingGlass
} from 'react-icons/hi2';
import MetricCard from '../components/common/MetricCard';
import StatusBadge from '../components/common/StatusBadge';
import Pagination from '../components/common/Pagination';
import { auditApi } from '../api/auditApi';
import { exportToCSV } from '../utils/exportUtils';
import { useNotificationContext } from '../context/NotificationContext';

/**
 * Member 06: Audit Logs & Security Telemetry Page
 *
 * Provides complete immutable audit trails of all user and system operations
 * (Logins, Employee additions, Payroll calculations, Leave decisions).
 */
const AuditLogs = () => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  
  const [logs, setLogs] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [totalRecords, setTotalRecords] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [searchActor, setSearchActor] = useState('');

  const fetchAuditLogs = async () => {
    setIsLoading(true);
    try {
      const res = await auditApi.getAuditLogs(page - 1, 15);
      setLogs(res.data || []);
      setTotalPages(res.totalPages || 1);
      setTotalRecords(res.totalElements || res.total || (res.data?.length || 0));
    } catch (err) {
      showToastError('Failed to fetch audit logs.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchAuditLogs();
  }, [page]);

  const handleExport = () => {
    exportToCSV(logs, 'GlobalPay_Security_Audit_Logs');
    showToastSuccess('Audit logs exported to CSV.');
  };

  const getActionIcon = (action) => {
    if (action?.includes('EMPLOYEE')) return HiOutlineUserPlus;
    if (action?.includes('PAYROLL')) return HiOutlineBanknotes;
    if (action?.includes('LEAVE')) return HiOutlineCalendarDays;
    if (action?.includes('ATTENDANCE')) return HiOutlineClock;
    return HiOutlineShieldCheck;
  };

  const displayedLogs = searchActor.trim()
    ? logs.filter((l) => 
        l.username?.toLowerCase().includes(searchActor.toLowerCase()) ||
        l.action?.toLowerCase().includes(searchActor.toLowerCase()) ||
        l.details?.toLowerCase().includes(searchActor.toLowerCase())
      )
    : logs;

  return (
    <div className="space-y-8 pb-10">
      {/* Top Banner */}
      <div className="p-6 bg-gradient-to-r from-[#191b23] to-[#2e3039] text-white rounded-2xl shadow-md relative overflow-hidden">
        <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <span className="bg-white/20 text-white text-[10px] font-mono font-extrabold uppercase px-2.5 py-0.5 rounded-full border border-white/30">
                Member 06 Module
              </span>
              <span className="text-xs font-semibold text-gray-300">Security & Compliance</span>
            </div>
            <h1 className="font-heading font-extrabold text-2xl lg:text-3xl mt-1 tracking-tight">
              Immutable System Audit Logs
            </h1>
            <p className="text-xs lg:text-sm text-gray-300 mt-1 max-w-2xl leading-relaxed">
              Every sensitive transactional event (Salary generation, Leave approval, Employee updates) is automatically recorded with actor, timestamp, IP address, and payload telemetry.
            </p>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={fetchAuditLogs}
              className="px-4 py-2.5 bg-white/10 hover:bg-white/20 text-white text-xs font-bold rounded-xl border border-white/20 flex items-center gap-2 transition-all shadow-xs"
            >
              <HiOutlineArrowPath className="w-4 h-4" />
              <span>Refresh Logs</span>
            </button>
            <button
              onClick={handleExport}
              className="px-5 py-2.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-extrabold rounded-xl flex items-center gap-2 transition-all shadow-md active:scale-[0.98]"
            >
              <HiOutlineArrowDownTray className="w-4 h-4" />
              <span>Export Audit Trail</span>
            </button>
          </div>
        </div>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <MetricCard
          title="RECORDED AUDIT EVENTS"
          value={String(totalRecords)}
          badge="Live Database Stream"
          badgeType="info"
          icon={HiOutlineClock}
        />
        <MetricCard
          title="ENCRYPTION / PROTOCOL"
          value="SHA-256 / TLS"
          badge="Enterprise Grade"
          badgeType="success"
          icon={HiOutlineShieldCheck}
        />
        <MetricCard
          title="ACTIVE AUDIT LOGGERS"
          value="6 Member Modules"
          subtext="Spring Boot AOP Interceptors"
        />
      </div>

      {/* Audit Log Table */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
        {/* Search Header */}
        <div className="p-6 border-b border-[#e1e2ed] flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h3 className="font-heading font-bold text-lg text-[#191b23]">
              Audit Trail Activity Logs ({displayedLogs.length})
            </h3>
            <p className="text-xs text-[#737686]">Chronological record of user and system events.</p>
          </div>

          <div className="w-full sm:w-72 relative">
            <HiOutlineMagnifyingGlass className="w-4 h-4 text-[#737686] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={searchActor}
              onChange={(e) => setSearchActor(e.target.value)}
              placeholder="Search by action, actor or details..."
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl pl-9 pr-3 py-2 text-xs text-[#191b23] focus:outline-none"
            />
          </div>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                <th className="py-4 px-6">Event / Action</th>
                <th className="py-4 px-6">Actor (User)</th>
                <th className="py-4 px-6">Target Entity</th>
                <th className="py-4 px-6">Event Details</th>
                <th className="py-4 px-6">IP Address</th>
                <th className="py-4 px-6">Timestamp</th>
                <th className="py-4 px-6 text-right">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
              {displayedLogs.length === 0 ? (
                <tr>
                  <td colSpan="7" className="py-8 text-center text-xs text-[#737686]">
                    {isLoading ? 'Loading system audit logs...' : 'No audit records found matching your query.'}
                  </td>
                </tr>
              ) : (
                displayedLogs.map((log) => {
                  const Icon = getActionIcon(log.action);
                  return (
                    <tr key={log.id} className="hover:bg-[#faf8ff] transition-colors">
                      <td className="py-4 px-6">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 rounded-xl bg-[#f3f3fe] border border-[#e1e2ed] flex items-center justify-center text-[#004ac6]">
                            <Icon className="w-4 h-4" />
                          </div>
                          <span className="font-bold text-[#191b23] text-xs">{log.action}</span>
                        </div>
                      </td>

                      <td className="py-4 px-6 font-semibold text-xs text-[#004ac6]">
                        {log.username || 'SYSTEM'}
                      </td>

                      <td className="py-4 px-6 font-mono text-xs text-[#434655]">
                        {log.entityName || 'GENERAL'}
                      </td>

                      <td className="py-4 px-6 text-xs text-[#434655] max-w-xs truncate" title={log.details}>
                        {log.details || 'Operational record'}
                      </td>

                      <td className="py-4 px-6 font-mono text-[11px] text-[#737686]">
                        {log.ipAddress || '127.0.0.1'}
                      </td>

                      <td className="py-4 px-6 text-xs text-[#737686]">
                        {log.createdAt ? new Date(log.createdAt).toLocaleString() : 'Recent'}
                      </td>

                      <td className="py-4 px-6 text-right">
                        <StatusBadge status="COMPLETED" />
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>

        <div className="p-4 border-t border-[#e1e2ed]">
          <Pagination
            currentPage={page}
            totalPages={totalPages}
            totalRecords={totalRecords}
            pageSize={15}
            onPageChange={(p) => setPage(p)}
          />
        </div>
      </div>
    </div>
  );
};

export default AuditLogs;
