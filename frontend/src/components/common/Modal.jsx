import React from 'react';
import { HiXMark } from 'react-icons/hi2';

/**
 * Reusable Dialog Modal Component
 *
 * Glassmorphic backdrop with smooth fade animation for forms, previews, and actions.
 */
const Modal = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[#191b23]/40 backdrop-blur-sm animate-fadeIn">
      <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-2xl max-w-xl w-full overflow-hidden transform transition-all">
        {/* Modal Header */}
        <div className="px-6 py-4 border-b border-[#e1e2ed] flex items-center justify-between bg-[#faf8ff]">
          <h3 className="font-heading font-bold text-lg text-[#191b23]">{title}</h3>
          <button
            onClick={onClose}
            aria-label="Close Dialog"
            className="w-8 h-8 rounded-full bg-[#f3f3fe] hover:bg-[#ededf9] flex items-center justify-center text-[#737686] transition-colors"
          >
            <HiXMark className="w-5 h-5" />
          </button>
        </div>

        {/* Modal Content */}
        <div className="p-6">{children}</div>
      </div>
    </div>
  );
};

export default Modal;
