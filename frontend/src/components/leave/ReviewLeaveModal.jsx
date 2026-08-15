import React, { useState } from 'react';
import Modal from '../common/Modal';
import { useNotificationContext } from '../../context/NotificationContext';

/**
 * Review Leave Modal Component (Approve / Reject with comments)
 */
const ReviewLeaveModal = ({ isOpen, onClose, request, onAction }) => {
  const { showToastSuccess, showToastError } = useNotificationContext();
  const [actionReason, setActionReason] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  if (!request) return null;

  const handleDecision = async (decision) => {
    setIsSubmitting(true);
    try {
      await onAction(request.id, decision, actionReason || (decision === 'APPROVE' ? 'Approved by HR' : 'Rejected by HR'));
      showToastSuccess(`Leave request ${decision === 'APPROVE' ? 'Approved' : 'Rejected'} successfully.`);
      setActionReason('');
      onClose();
    } catch (err) {
      showToastError(err.response?.data?.message || 'Failed to process leave decision.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`Review Leave: ${request.employeeName}`}>
      <div className="space-y-4">
        <div className="p-4 bg-[#f3f3fe] border border-[#e1e2ed] rounded-2xl space-y-2 text-xs">
          <div className="flex justify-between">
            <span className="text-[#737686] font-bold">Leave Type:</span>
            <span className="font-bold text-[#004ac6]">{request.leaveType}</span>
          </div>
          <div className="flex justify-between">
            <span className="text-[#737686] font-bold">Duration:</span>
            <span className="font-semibold text-[#191b23]">{request.durationDays || request.totalDays} Days ({request.startDate} to {request.endDate})</span>
          </div>
          <div>
            <span className="text-[#737686] font-bold">Employee Reason:</span>
            <p className="font-medium text-[#191b23] mt-1 bg-white p-2.5 rounded-xl border border-[#e1e2ed]">{request.reason || 'No reason provided.'}</p>
          </div>
        </div>

        <div>
          <label className="block text-xs font-bold uppercase tracking-wider text-[#191b23] mb-1">
            Reviewer Notes / Feedback
          </label>
          <textarea
            rows="2"
            value={actionReason}
            onChange={(e) => setActionReason(e.target.value)}
            placeholder="e.g. Approved as per quarterly quota policy..."
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3.5 py-2 text-sm text-[#191b23] focus:outline-none"
          ></textarea>
        </div>

        <div className="flex items-center justify-end gap-3 pt-4 border-t border-[#e1e2ed]">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2.5 rounded-xl border border-[#c3c6d7] text-sm font-semibold text-[#434655] hover:bg-[#ededf9]"
          >
            Cancel
          </button>
          <button
            type="button"
            disabled={isSubmitting}
            onClick={() => handleDecision('REJECT')}
            className="px-4 py-2.5 rounded-xl bg-[#fee2e2] hover:bg-[#fecaca] text-[#991b1b] text-sm font-semibold transition-all disabled:opacity-50"
          >
            Reject Request
          </button>
          <button
            type="button"
            disabled={isSubmitting}
            onClick={() => handleDecision('APPROVE')}
            className="px-5 py-2.5 rounded-xl bg-[#004ac6] hover:bg-[#2563eb] text-white text-sm font-semibold shadow-md disabled:opacity-50"
          >
            Approve Request
          </button>
        </div>
      </div>
    </Modal>
  );
};

export default ReviewLeaveModal;
