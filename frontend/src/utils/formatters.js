/**
 * Utility Formatters
 *
 * Provides formatting functions for numbers, currency, percentages, dates, and initials
 * across all modules of GlobalPay Enterprise HRMS.
 */

/**
 * Formats a number as USD Currency (e.g., $4,200,000.00 or $4.2M)
 * @param {number} value - Numeric amount
 * @param {boolean} compact - If true, formats as $4.2M / $582K
 * @returns {string} Formatted currency string
 */
export const formatCurrency = (value, compact = false) => {
  if (value === null || value === undefined || isNaN(value)) return '$0.00';

  if (compact) {
    if (Math.abs(value) >= 1_000_000) {
      return `$${(value / 1_000_000).toFixed(1)}M`;
    }
    if (Math.abs(value) >= 1_000) {
      return `$${(value / 1_000).toFixed(0)}K`;
    }
  }

  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(value);
};

/**
 * Formats numeric count with commas (e.g., 1248 -> 1,248)
 * @param {number} num 
 * @returns {string}
 */
export const formatNumber = (num) => {
  if (num === null || num === undefined || isNaN(num)) return '0';
  return new Intl.NumberFormat('en-US').format(num);
};

/**
 * Formats percentage value (e.g., 0.12 -> 12% or +12%)
 * @param {number} value 
 * @param {boolean} includeSign 
 * @returns {string}
 */
export const formatPercent = (value, includeSign = false) => {
  if (value === null || value === undefined || isNaN(value)) return '0%';
  const prefix = includeSign && value > 0 ? '+' : '';
  return `${prefix}${value}%`;
};

/**
 * Formats ISO date string to human readable format (e.g., Oct 24, 2023 • 09:45 AM)
 * @param {string|Date} dateInput 
 * @param {boolean} includeTime 
 * @returns {string}
 */
export const formatDate = (dateInput, includeTime = false) => {
  if (!dateInput) return '-';
  const date = new Date(dateInput);
  if (isNaN(date.getTime())) return '-';

  const options = {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  };

  if (includeTime) {
    options.hour = '2-digit';
    options.minute = '2-digit';
    options.hour12 = true;
  }

  return new Intl.DateTimeFormat('en-US', options).format(date);
};

/**
 * Extract 2-letter uppercase initials from full name (e.g., "Sarah Jenkins" -> "SJ")
 * @param {string} name 
 * @returns {string}
 */
export const getInitials = (name) => {
  if (!name) return 'GP';
  const parts = name.trim().split(' ');
  if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
  return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
};
