import React from 'react';

/**
 * Status Badge Component
 *
 * Renders pill badges with exact color parameters:
 * - Active: Green dot + light green background
 * - On Leave: Amber dot + soft orange/yellow background
 * - Completed: Solid green text badge
 * - Pending: Soft blue badge
 * - Reviewing: Neutral slate badge
 * - Paid: Bright mint green badge
 */
const StatusBadge = ({ status = 'Active' }) => {
  const normalized = status ? status.toUpperCase() : 'ACTIVE';

  if (normalized === 'ACTIVE') {
    return (
      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-[#dcfce7] text-[#166534]">
        <span className="w-1.5 h-1.5 rounded-full bg-[#16a34a]"></span>
        Active
      </span>
    );
  }

  if (normalized === 'ON LEAVE' || normalized === 'LEAVE') {
    return (
      <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-[#fef3c7] text-[#92400e]">
        <span className="w-1.5 h-1.5 rounded-full bg-[#d97706]"></span>
        On Leave
      </span>
    );
  }

  if (normalized === 'COMPLETED') {
    return (
      <span className="inline-flex items-center px-3 py-1 rounded-full text-[11px] font-bold tracking-wider bg-[#dcfce7] text-[#15803d]">
        COMPLETED
      </span>
    );
  }

  if (normalized === 'PENDING') {
    return (
      <span className="inline-flex items-center px-3 py-1 rounded-full text-[11px] font-bold tracking-wider bg-[#e0f2fe] text-[#0369a1]">
        PENDING
      </span>
    );
  }

  if (normalized === 'REVIEWING') {
    return (
      <span className="inline-flex items-center px-3 py-1 rounded-full text-[11px] font-bold tracking-wider bg-[#f1f5f9] text-[#475569]">
        REVIEWING
      </span>
    );
  }

  if (normalized === 'PAID') {
    return (
      <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-extrabold tracking-wide bg-[#dcfce7] text-[#166534]">
        PAID
      </span>
    );
  }

  return (
    <span className="inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-[#f3f4f6] text-[#374151]">
      {status}
    </span>
  );
};

export default StatusBadge;
