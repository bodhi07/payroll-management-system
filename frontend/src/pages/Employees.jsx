import React, { useState, useEffect } from 'react';
import { 
  HiOutlineUserPlus, 
  HiOutlineArrowDownTray, 
  HiOutlineMagnifyingGlass, 
  HiOutlineEye, 
  HiOutlinePencilSquare,
  HiOutlineTrash,
  HiOutlineCheckCircle,
  HiOutlineLifebuoy,
  HiOutlineChartBar,
  HiOutlineCommandLine
} from 'react-icons/hi2';
import StatusBadge from '../components/common/StatusBadge';
import Pagination from '../components/common/Pagination';
import AddEmployeeModal from '../components/employee/AddEmployeeModal';
import EditEmployeeModal from '../components/employee/EditEmployeeModal';
import ViewEmployeeModal from '../components/employee/ViewEmployeeModal';
import { useEmployees } from '../hooks/useEmployees';
import { departmentApi } from '../api/departmentApi';
import { exportToCSV } from '../utils/exportUtils';
import { useNotificationContext } from '../context/NotificationContext';

/**
 * Employee Management Page Component
 *
 * Full CRUD, real-time search, department filtering, linear search algorithm tester,
 * pagination, and live workforce metrics.
 */
const Employees = () => {
  const { showToastSuccess, showToastError, showConfirmDialog } = useNotificationContext();
  
  const [activeTab, setActiveTab] = useState('directory');
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [selectedEmployeeForView, setSelectedEmployeeForView] = useState(null);
  const [selectedEmployeeForEdit, setSelectedEmployeeForEdit] = useState(null);
  
  const [search, setSearch] = useState('');
  const [departmentId, setDepartmentId] = useState('All');
  const [status, setStatus] = useState('All');
  const [page, setPage] = useState(1);
  const [departments, setDepartments] = useState([]);

  // Linear search state
  const [linearKeyword, setLinearKeyword] = useState('');
  const [linearResults, setLinearResults] = useState(null);
  const [isLinearSearching, setIsLinearSearching] = useState(false);

  useEffect(() => {
    departmentApi.getDepartments().then(setDepartments).catch(() => {});
  }, []);

  const { 
    employees, 
    totalRecords, 
    totalPages, 
    isLoading, 
    createEmployee, 
    updateEmployee, 
    deleteEmployee 
  } = useEmployees({
    search,
    departmentId,
    status,
    page,
    pageSize: 10,
  });

  const handleDelete = async (id, name) => {
    const confirmed = await showConfirmDialog({
      title: `Delete Employee?`,
      text: `Are you sure you want to permanently delete ${name}? This action cannot be undone.`,
      confirmButtonText: 'Yes, Delete Employee',
    });

    if (confirmed) {
      try {
        await deleteEmployee(id);
        showToastSuccess(`Employee ${name} deleted successfully.`);
      } catch (err) {
        showToastError(err.response?.data?.message || 'Failed to delete employee.');
      }
    }
  };

  const handleLinearSearch = async (e) => {
    e.preventDefault();
    if (!linearKeyword.trim()) {
      setLinearResults(null);
      return;
    }
    setIsLinearSearching(true);
    try {
      const { employeeService } = await import('../services/employeeService');
      const res = await employeeService.linearSearch(linearKeyword.trim());
      setLinearResults(res || []);
      showToastSuccess(`Linear search executed on backend: ${res?.length || 0} match(es) found.`);
    } catch (err) {
      showToastError('Linear search algorithm failed.');
    } finally {
      setIsLinearSearching(false);
    }
  };

  const handleExport = () => {
    exportToCSV(employees, 'GlobalPay_Employees_Directory');
    showToastSuccess('Employee records exported to CSV successfully.');
  };

  const displayedEmployees = linearResults !== null ? linearResults : employees;

  return (
    <div className="space-y-6 pb-8">
      {/* Top Sub-nav Tabs */}
      <div className="flex items-center gap-6 border-b border-[#e1e2ed] pb-3">
        <button
          onClick={() => { setActiveTab('directory'); setLinearResults(null); }}
          className={`text-sm font-bold pb-2 transition-colors relative ${
            activeTab === 'directory'
              ? 'text-[#004ac6] border-b-2 border-[#004ac6]'
              : 'text-[#737686] hover:text-[#191b23]'
          }`}
        >
          Directory
        </button>
        <button
          onClick={() => setActiveTab('algorithm')}
          className={`text-sm font-bold pb-2 transition-colors flex items-center gap-1.5 ${
            activeTab === 'algorithm'
              ? 'text-[#004ac6] border-b-2 border-[#004ac6]'
              : 'text-[#737686] hover:text-[#191b23]'
          }`}
        >
          <HiOutlineCommandLine className="w-4 h-4" />
          <span>Linear Search Algorithm (DSA)</span>
        </button>
      </div>

      {/* Main Title & Header Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
            Employee Directory
          </h1>
          <p className="text-sm text-[#737686] mt-1">
            Manage your workforce with real-time Spring Boot CRUD and data operations.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={handleExport}
            className="px-4 py-2.5 bg-white border border-[#c3c6d7] hover:bg-[#f3f3fe] text-[#191b23] text-sm font-semibold rounded-xl flex items-center gap-2 shadow-xs transition-colors"
          >
            <HiOutlineArrowDownTray className="w-4 h-4" />
            <span>Export CSV</span>
          </button>
          <button
            onClick={() => setIsAddModalOpen(true)}
            className="px-4 py-2.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-sm font-semibold rounded-xl flex items-center gap-2 shadow-md transition-all active:scale-[0.98]"
          >
            <HiOutlineUserPlus className="w-4 h-4" />
            <span>Add Employee</span>
          </button>
        </div>
      </div>

      {/* DSA Algorithm Section */}
      {activeTab === 'algorithm' && (
        <div className="p-5 bg-[#eff6ff] border border-[#bfdbfe] rounded-2xl space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="font-heading font-bold text-sm text-[#1e40af] flex items-center gap-2">
              <HiOutlineCommandLine className="w-5 h-5" />
              Custom Linear Search Algorithm Test (Member 01 Core Algorithm)
            </h3>
            {linearResults !== null && (
              <button
                onClick={() => setLinearResults(null)}
                className="text-xs font-bold text-[#1e40af] underline"
              >
                Reset Search
              </button>
            )}
          </div>
          <form onSubmit={handleLinearSearch} className="flex gap-3">
            <input
              type="text"
              value={linearKeyword}
              onChange={(e) => setLinearKeyword(e.target.value)}
              placeholder="Search keyword (e.g. Agrani, John, Sarah)..."
              className="flex-1 bg-white border border-[#bfdbfe] rounded-xl px-4 py-2 text-sm text-[#191b23] focus:outline-none"
            />
            <button
              type="submit"
              disabled={isLinearSearching}
              className="px-5 py-2 bg-[#1e40af] hover:bg-[#1d4ed8] text-white text-xs font-bold rounded-xl transition-colors"
            >
              {isLinearSearching ? 'Searching...' : 'Run Linear Search'}
            </button>
          </form>
        </div>
      )}

      {/* Search & Filter Controls Bar */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl p-5 shadow-xs grid grid-cols-1 sm:grid-cols-3 gap-4">
        {/* Search Input */}
        <div>
          <label className="block text-[11px] font-bold uppercase tracking-wider text-[#737686] mb-1.5">
            Search
          </label>
          <div className="relative">
            <HiOutlineMagnifyingGlass className="w-4 h-4 text-[#737686] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={search}
              onChange={(e) => { setSearch(e.target.value); setPage(1); setLinearResults(null); }}
              placeholder="Name, Employee No, NIC or Email..."
              className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl pl-9 pr-3 py-2 text-sm text-[#191b23] placeholder-[#737686] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6]"
            />
          </div>
        </div>

        {/* Department Select */}
        <div>
          <label className="block text-[11px] font-bold uppercase tracking-wider text-[#737686] mb-1.5">
            Department
          </label>
          <select
            value={departmentId}
            onChange={(e) => { setDepartmentId(e.target.value); setPage(1); setLinearResults(null); }}
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3 py-2 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6]"
          >
            <option value="All">All Departments</option>
            {departments.map((d) => (
              <option key={d.id} value={d.id}>{d.name} ({d.code})</option>
            ))}
          </select>
        </div>

        {/* Status Select */}
        <div>
          <label className="block text-[11px] font-bold uppercase tracking-wider text-[#737686] mb-1.5">
            Status
          </label>
          <select
            value={status}
            onChange={(e) => { setStatus(e.target.value); setPage(1); setLinearResults(null); }}
            className="w-full bg-[#f3f3fe] border border-[#c3c6d7] rounded-xl px-3 py-2 text-sm text-[#191b23] focus:outline-none focus:ring-2 focus:ring-[#2563eb]/20 focus:border-[#004ac6]"
          >
            <option value="All">All Statuses</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>
            <option value="TERMINATED">TERMINATED</option>
          </select>
        </div>
      </div>

      {/* Employee Data Table Card */}
      <div className="bg-white border border-[#e1e2ed] rounded-2xl shadow-xs overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#f3f3fe]/70 border-b border-[#e1e2ed] text-[11px] font-extrabold uppercase tracking-wider text-[#737686]">
                <th className="py-4 px-6">Employee</th>
                <th className="py-4 px-6">ID / Number</th>
                <th className="py-4 px-6">Department</th>
                <th className="py-4 px-6">Designation</th>
                <th className="py-4 px-6">Status</th>
                <th className="py-4 px-6 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#e1e2ed]/60 text-sm">
              {displayedEmployees.length === 0 ? (
                <tr>
                  <td colSpan="6" className="py-8 text-center text-xs text-[#737686]">
                    {isLoading ? 'Loading employee records from Spring Boot backend...' : 'No employees matching your criteria.'}
                  </td>
                </tr>
              ) : (
                displayedEmployees.map((emp) => (
                  <tr key={emp.id} className="hover:bg-[#faf8ff] transition-colors">
                    {/* Avatar + Name + Email */}
                    <td className="py-4 px-6">
                      <div className="flex items-center gap-3.5">
                        <img
                          src={emp.avatar || `https://ui-avatars.com/api/?name=${encodeURIComponent(emp.name)}&background=004ac6&color=fff`}
                          alt={emp.name}
                          className="w-10 h-10 rounded-full object-cover ring-1 ring-[#e1e2ed]"
                        />
                        <div>
                          <p className="font-bold text-[#191b23] leading-tight">{emp.name}</p>
                          <p className="text-xs text-[#737686]">{emp.email}</p>
                        </div>
                      </div>
                    </td>

                    {/* Employee ID */}
                    <td className="py-4 px-6 font-mono text-xs font-semibold text-[#434655]">
                      {emp.employeeId || emp.employeeNumber}
                    </td>

                    {/* Department */}
                    <td className="py-4 px-6 text-xs font-semibold text-[#191b23]">
                      {emp.department}
                    </td>

                    {/* Designation */}
                    <td className="py-4 px-6 text-xs font-medium text-[#434655]">
                      {emp.designation}
                    </td>

                    {/* Status Badge */}
                    <td className="py-4 px-6">
                      <StatusBadge status={emp.status} />
                    </td>

                    {/* Action Icons */}
                    <td className="py-4 px-6 text-right">
                      <div className="flex items-center justify-end gap-3 text-[#737686]">
                        <button
                          title="View Profile"
                          onClick={() => setSelectedEmployeeForView(emp)}
                          className="hover:text-[#004ac6] transition-colors p-1"
                        >
                          <HiOutlineEye className="w-5 h-5" />
                        </button>
                        <button
                          title="Edit Employee"
                          onClick={() => setSelectedEmployeeForEdit(emp)}
                          className="hover:text-[#004ac6] transition-colors p-1"
                        >
                          <HiOutlinePencilSquare className="w-5 h-5" />
                        </button>
                        <button
                          title="Delete Employee"
                          onClick={() => handleDelete(emp.id, emp.name)}
                          className="hover:text-[#ba1a1a] transition-colors p-1"
                        >
                          <HiOutlineTrash className="w-5 h-5" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Table Pagination */}
        <div className="p-4 border-t border-[#e1e2ed]">
          <Pagination
            currentPage={page}
            totalPages={totalPages}
            totalRecords={totalRecords}
            pageSize={10}
            onPageChange={(p) => setPage(p)}
          />
        </div>
      </div>

      {/* Bottom Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs flex items-center gap-5">
          <div className="w-12 h-12 rounded-2xl bg-[#dcfce7] text-[#166534] flex items-center justify-center">
            <HiOutlineCheckCircle className="w-6 h-6" />
          </div>
          <div>
            <p className="text-xs font-bold text-[#737686] uppercase tracking-wider">Active Employees</p>
            <h3 className="font-heading font-extrabold text-2xl text-[#191b23] mt-1">
              {employees.filter((e) => e.status === 'ACTIVE').length}
            </h3>
          </div>
        </div>

        <div className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs flex items-center gap-5">
          <div className="w-12 h-12 rounded-2xl bg-[#fef3c7] text-[#92400e] flex items-center justify-center">
            <HiOutlineLifebuoy className="w-6 h-6" />
          </div>
          <div>
            <p className="text-xs font-bold text-[#737686] uppercase tracking-wider">Total Database Records</p>
            <h3 className="font-heading font-extrabold text-2xl text-[#191b23] mt-1">
              {totalRecords}
            </h3>
          </div>
        </div>

        <div className="relative bg-[#004ac6] text-white rounded-2xl p-6 shadow-md overflow-hidden flex flex-col justify-between">
          <div className="absolute right-0 bottom-0 opacity-20 pointer-events-none">
            <HiOutlineChartBar className="w-32 h-32 -mr-6 -mb-6" />
          </div>
          <div className="relative z-10">
            <p className="text-xs font-semibold uppercase tracking-wider text-blue-200">
              Active Departments
            </p>
            <h3 className="font-heading font-extrabold text-3xl mt-2">{departments.length}</h3>
          </div>
        </div>
      </div>

      {/* Modals */}
      <AddEmployeeModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onAdd={createEmployee}
      />

      <EditEmployeeModal
        isOpen={!!selectedEmployeeForEdit}
        employee={selectedEmployeeForEdit}
        onClose={() => setSelectedEmployeeForEdit(null)}
        onUpdate={updateEmployee}
      />

      <ViewEmployeeModal
        isOpen={!!selectedEmployeeForView}
        employee={selectedEmployeeForView}
        onClose={() => setSelectedEmployeeForView(null)}
      />
    </div>
  );
};

export default Employees;
