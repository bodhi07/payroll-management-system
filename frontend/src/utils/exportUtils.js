/**
 * Export Utilities
 *
 * Handles CSV/Excel report exports, dataset downloads, and browser PDF printing
 * for the Payroll, Employee, and Reports modules.
 */

/**
 * Downloads a JSON array of objects as a CSV file in the browser.
 * @param {Array<Object>} data - Array of data rows
 * @param {string} filename - Output filename (e.g. payroll_report_oct_2023)
 */
export const exportToCSV = (data, filename = 'export') => {
  if (!data || !data.length) {
    console.warn('Export canceled: Data array is empty.');
    return;
  }

  // Extract CSV headers from first object keys
  const headers = Object.keys(data[0]);
  const csvRows = [];

  // Add header row
  csvRows.push(headers.map(header => `"${header}"`).join(','));

  // Add data rows
  for (const row of data) {
    const values = headers.map(header => {
      const escaped = ('' + (row[header] ?? '')).replace(/"/g, '""');
      return `"${escaped}"`;
    });
    csvRows.push(values.join(','));
  }

  const csvString = csvRows.join('\n');
  const blob = new Blob([csvString], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  
  link.setAttribute('href', url);
  link.setAttribute('download', `${filename}_${new Date().toISOString().slice(0, 10)}.csv`);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

/**
 * Triggers native browser print dialog specifically styled for PDF Payslip export
 */
export const printPayslip = () => {
  window.print();
};
