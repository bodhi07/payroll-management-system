import React from 'react';
import { HiChevronLeft, HiChevronRight } from 'react-icons/hi2';

/**
 * Pagination Component
 *
 * Reusable pagination bar matching exact UI design system specifications.
 */
const Pagination = ({
  currentPage = 1,
  totalPages = 490,
  totalRecords = 2450,
  pageSize = 5,
  onPageChange = () => {},
}) => {
  const startRecord = (currentPage - 1) * pageSize + 1;
  const endRecord = Math.min(currentPage * pageSize, totalRecords);

  return (
    <div className="flex flex-col sm:flex-row items-center justify-between gap-4 pt-4 px-2">
      {/* Total records indicator */}
      <p className="text-sm font-medium text-[#737686]">
        Showing <span className="font-semibold text-[#191b23]">{startRecord}</span> to{' '}
        <span className="font-semibold text-[#191b23]">{endRecord}</span> of{' '}
        <span className="font-semibold text-[#191b23]">{totalRecords.toLocaleString()}</span> employees
      </p>

      {/* Pagination control buttons */}
      <div className="flex items-center gap-1.5">
        <button
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 1}
          aria-label="Previous Page"
          className="w-9 h-9 rounded-lg border border-[#e1e2ed] flex items-center justify-center text-[#434655] hover:bg-[#ededf9] disabled:opacity-40 disabled:cursor-not-allowed transition-colors"
        >
          <HiChevronLeft className="w-4 h-4" />
        </button>

        {/* Page 1 */}
        <button
          onClick={() => onPageChange(1)}
          className={`w-9 h-9 rounded-lg font-medium text-sm transition-colors ${
            currentPage === 1
              ? 'bg-[#004ac6] text-white font-semibold shadow-xs'
              : 'text-[#434655] hover:bg-[#ededf9]'
          }`}
        >
          1
        </button>

        {/* Page 2 */}
        <button
          onClick={() => onPageChange(2)}
          className={`w-9 h-9 rounded-lg font-medium text-sm transition-colors ${
            currentPage === 2
              ? 'bg-[#004ac6] text-white font-semibold shadow-xs'
              : 'text-[#434655] hover:bg-[#ededf9]'
          }`}
        >
          2
        </button>

        {/* Page 3 */}
        <button
          onClick={() => onPageChange(3)}
          className={`w-9 h-9 rounded-lg font-medium text-sm transition-colors ${
            currentPage === 3
              ? 'bg-[#004ac6] text-white font-semibold shadow-xs'
              : 'text-[#434655] hover:bg-[#ededf9]'
          }`}
        >
          3
        </button>

        {/* Ellipsis */}
        <span className="px-1 text-sm font-medium text-[#737686]">...</span>

        {/* Page 490 */}
        <button
          onClick={() => onPageChange(totalPages)}
          className={`w-9 h-9 rounded-lg font-medium text-sm transition-colors ${
            currentPage === totalPages
              ? 'bg-[#004ac6] text-white font-semibold shadow-xs'
              : 'text-[#434655] hover:bg-[#ededf9]'
          }`}
        >
          {totalPages}
        </button>

        <button
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage === totalPages}
          aria-label="Next Page"
          className="w-9 h-9 rounded-lg border border-[#e1e2ed] flex items-center justify-center text-[#434655] hover:bg-[#ededf9] disabled:opacity-40 disabled:cursor-not-allowed transition-colors"
        >
          <HiChevronRight className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
};

export default Pagination;
