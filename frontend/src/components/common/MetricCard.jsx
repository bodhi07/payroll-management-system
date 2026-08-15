import React from 'react';

/**
 * Statistics & Metric Card Component
 *
 * Base card used in Dashboard, Employee Directory, Payroll, and Reports modules.
 */
const MetricCard = ({
  title,
  value,
  badge,
  badgeType = 'success',
  subtext,
  icon: Icon,
  children,
  className = '',
}) => {
  return (
    <div className={`bg-white border border-[#e1e2ed] rounded-xl p-5 shadow-xs transition-shadow hover:shadow-md ${className}`}>
      <div className="flex items-start justify-between">
        <div>
          <p className="text-xs font-semibold text-[#737686] uppercase tracking-wider">{title}</p>
          <div className="flex items-baseline gap-2.5 mt-2">
            <h3 className="font-heading font-extrabold text-2xl text-[#191b23] tracking-tight">{value}</h3>
            {badge && (
              <span
                className={`text-xs font-bold px-2 py-0.5 rounded-md ${
                  badgeType === 'success'
                    ? 'bg-[#dcfce7] text-[#15803d]'
                    : badgeType === 'danger'
                    ? 'bg-[#fee2e2] text-[#991b1b]'
                    : 'bg-[#e0f2fe] text-[#0369a1]'
                }`}
              >
                {badge}
              </span>
            )}
          </div>
          {subtext && <p className="text-xs font-medium text-[#737686] mt-2">{subtext}</p>}
        </div>
        {Icon && (
          <div className="w-10 h-10 rounded-xl bg-[#f3f3fe] border border-[#e1e2ed] flex items-center justify-center text-[#004ac6]">
            <Icon className="w-5 h-5" />
          </div>
        )}
      </div>
      {children && <div className="mt-4">{children}</div>}
    </div>
  );
};

export default MetricCard;
