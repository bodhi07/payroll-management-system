import React from 'react';

/**
 * Loading Spinner Component
 *
 * Provides a clean animated spinner for async operations and React Query loading states.
 */
const Loader = ({ size = 'medium', message = 'Loading payroll analytics...' }) => {
  const sizeClasses = {
    small: 'w-5 h-5 border-2',
    medium: 'w-8 h-8 border-3',
    large: 'w-12 h-12 border-4',
  };

  return (
    <div className="flex flex-col items-center justify-center p-8 gap-3">
      <div
        className={`${sizeClasses[size]} border-[#004ac6] border-t-transparent rounded-full animate-spin`}
      ></div>
      {message && <p className="text-xs font-semibold text-[#737686]">{message}</p>}
    </div>
  );
};

export default Loader;
