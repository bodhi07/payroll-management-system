import React, { useState, useEffect } from 'react';
import { HiOutlineShieldCheck, HiOutlineUserPlus, HiOutlineBanknotes, HiOutlineCalendarDays, HiOutlineClock } from 'react-icons/hi2';
import StatusBadge from '../common/StatusBadge';
import { auditApi } from '../../api/auditApi';

/**
 * Recent Activity Table Component
 *
 * Renders live audit activities fetched from Spring Boot /api/v1/audit-logs.
 */
const RecentActivityTable = () => {
  const [activities, setActivities] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    auditApi.getAuditLogs(0, 10).then((res) => {
      setActivities(res.data || []);
      setIsLoading(false);
    }).catch(() => {
      setIsLoading(false);
    });
  }, []);

  const getActionIcon = (action) => {
    if (action?.includes('EMPLOYEE')) return HiOutlineUserPlus;
    if (action?.includes('PAYROLL')) return HiOutlineBanknotes;
    if (action?.includes('LEAVE')) return HiOutlineCalendarDays;
    if (action?.includes('ATTENDANCE')) return HiOutlineClock;
    return HiOutlineShieldCheck;
  };

  return (
    <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
      {/* Header */}
      <div className="px-6 py-5 border-b border-[#e1e2ed] flex items-center justify-between">
        <h3 className="font-heading font-bold text-lg text-[#191b23]">Recent System Audit Trail</h3>
        <span className="text-xs font-semibold text-[#737686]">Live Spring Boot Audit Logs</span>
      </div>

      {/* Table */}
      <div className="overflow-x-auto">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-[#f3f3fe]/60 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
              <th className="py-3.5 px-6">Action / Event</th>
              <th className="py-3.5 px-6">User / Actor</th>
              <th className="py-3.5 px-6">Entity Involved</th>
              <th className="py-3.5 px-6">Date & Time</th>
              <th className="py-3.5 px-6 text-right">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
            {activities.length === 0 ? (
              <tr>
                <td colSpan="5" className="py-8 text-center text-xs text-[#737686]">
                  {isLoading ? 'Loading system audit logs...' : 'No audit records found.'}
                </td>
              </tr>
            ) : (
              activities.map((act) => {
                const Icon = getActionIcon(act.action);
                return (
                  <tr key={act.id} className="hover:bg-[#faf8ff] transition-colors">
                    <td className="py-4 px-6">
                      <div className="flex items-center gap-3.5">
                        <div className="w-9 h-9 rounded-full bg-[#f3f3fe] border border-[#e1e2ed] flex items-center justify-center text-[#004ac6]">
                          <Icon className="w-4 h-4" />
                        </div>
                        <div>
                          <p className="font-semibold text-[#191b23]">{act.action}</p>
                          <p className="text-xs text-[#737686]">{act.details || 'System event recorded'}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-4 px-6 text-xs font-semibold text-[#434655]">
                      {act.username || 'SYSTEM'}
                    </td>
                    <td className="py-4 px-6 text-xs font-mono text-[#004ac6]">
                      {act.entityName || 'General'}
                    </td>
                    <td className="py-4 px-6 text-xs font-medium text-[#737686]">
                      {act.createdAt ? new Date(act.createdAt).toLocaleString() : 'Recent'}
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
    </div>
  );
};

export default RecentActivityTable;
