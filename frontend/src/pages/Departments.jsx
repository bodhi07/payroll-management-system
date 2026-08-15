import React, { useState, useEffect } from 'react';
import { 
  HiOutlineBuildingOffice2, 
  HiOutlineUsers, 
  HiOutlineBanknotes, 
  HiOutlinePlusCircle,
  HiOutlinePencilSquare,
  HiOutlineTrash
} from 'react-icons/hi2';
import AddDepartmentModal from '../components/department/AddDepartmentModal';
import EditDepartmentModal from '../components/department/EditDepartmentModal';
import { departmentService } from '../services/departmentService';
import { formatCurrency } from '../utils/formatters';
import { useNotificationContext } from '../context/NotificationContext';

/**
 * Department Management Page Component
 *
 * Full Department CRUD, live sector headcount, and department salary budget aggregation reports.
 */
const Departments = () => {
  const { showToastSuccess, showToastError, showConfirmDialog } = useNotificationContext();
  
  const [departments, setDepartments] = useState([]);
  const [reports, setReports] = useState({});
  const [isLoading, setIsLoading] = useState(true);

  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [selectedDeptForEdit, setSelectedDeptForEdit] = useState(null);

  const fetchDepartmentsData = async () => {
    setIsLoading(true);
    try {
      const [deptList, reportList] = await Promise.all([
        departmentService.getDepartments({ pageSize: 50 }),
        departmentService.getAllDepartmentReports().catch(() => []),
      ]);
      setDepartments(deptList || []);

      const reportMap = {};
      (reportList || []).forEach((r) => {
        reportMap[r.departmentId] = r;
      });
      setReports(reportMap);
    } catch (err) {
      showToastError('Failed to fetch departments.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchDepartmentsData();
  }, []);

  const handleCreate = async (data) => {
    await departmentService.createDepartment(data);
    await fetchDepartmentsData();
  };

  const handleUpdate = async (id, data) => {
    await departmentService.updateDepartment(id, data);
    await fetchDepartmentsData();
  };

  const handleDelete = async (id, name) => {
    const confirmed = await showConfirmDialog({
      title: 'Delete Department?',
      text: `Are you sure you want to delete ${name}? Make sure no employees are assigned to this department.`,
      confirmButtonText: 'Yes, Delete',
    });

    if (confirmed) {
      try {
        await departmentService.deleteDepartment(id);
        showToastSuccess(`Department ${name} deleted successfully.`);
        await fetchDepartmentsData();
      } catch (err) {
        showToastError(err.response?.data?.message || 'Cannot delete department with active employees.');
      }
    }
  };

  return (
    <div className="space-y-6 pb-8">
      {/* Top Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="font-heading font-extrabold text-3xl text-[#191b23] tracking-tight">
            Department Management
          </h1>
          <p className="text-sm text-[#737686] mt-1">
            Corporate organizational hierarchy, headcount metrics, and basic salary budgets.
          </p>
        </div>

        <button
          onClick={() => setIsAddModalOpen(true)}
          className="px-5 py-2.5 bg-[#004ac6] hover:bg-[#2563eb] text-white text-xs font-bold rounded-xl flex items-center gap-2 shadow-md transition-all active:scale-[0.98]"
        >
          <HiOutlinePlusCircle className="w-4 h-4" />
          <span>Add Department</span>
        </button>
      </div>

      {/* Departments Grid */}
      {departments.length === 0 ? (
        <div className="p-12 text-center text-xs text-[#737686] bg-white rounded-2xl border border-[#e1e2ed]">
          {isLoading ? 'Loading corporate departments...' : 'No departments found. Click "Add Department" to create one.'}
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {departments.map((dept) => {
            const report = reports[dept.id] || reports[dept.departmentId] || {};
            const count = report.employeeCount ?? dept.headcount ?? 0;
            const budget = report.totalBasicSalaryBudget ?? 0;

            return (
              <div
                key={dept.id}
                className="bg-white border border-[#e1e2ed] rounded-2xl p-6 shadow-xs flex flex-col justify-between hover:shadow-md transition-all"
              >
                <div>
                  {/* Card Header & Code */}
                  <div className="flex items-start justify-between">
                    <div>
                      <span className="text-[10px] font-mono font-bold uppercase tracking-wider text-[#004ac6] bg-[#dbe1ff] px-2.5 py-0.5 rounded-md">
                        {dept.code}
                      </span>
                      <h3 className="font-heading font-extrabold text-xl text-[#191b23] mt-2">
                        {dept.name}
                      </h3>
                      {dept.description && (
                        <p className="text-xs text-[#737686] mt-1 leading-relaxed line-clamp-2">
                          {dept.description}
                        </p>
                      )}
                    </div>
                    <div className="w-10 h-10 rounded-xl bg-[#f3f3fe] text-[#004ac6] flex items-center justify-center shrink-0">
                      <HiOutlineBuildingOffice2 className="w-5 h-5" />
                    </div>
                  </div>

                  {/* Report Statistics */}
                  <div className="mt-5 grid grid-cols-2 gap-3 text-xs">
                    <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
                      <div className="flex items-center gap-1.5 text-[#737686]">
                        <HiOutlineUsers className="w-3.5 h-3.5" />
                        <span className="font-semibold uppercase tracking-wider text-[10px]">Headcount</span>
                      </div>
                      <p className="font-heading font-extrabold text-lg text-[#191b23] mt-1">
                        {count} Staff
                      </p>
                    </div>

                    <div className="p-3 bg-[#faf8ff] rounded-xl border border-[#e1e2ed]">
                      <div className="flex items-center gap-1.5 text-[#737686]">
                        <HiOutlineBanknotes className="w-3.5 h-3.5" />
                        <span className="font-semibold uppercase tracking-wider text-[10px]">Monthly Budget</span>
                      </div>
                      <p className="font-heading font-extrabold text-sm text-[#004ac6] mt-1">
                        {formatCurrency(budget)}
                      </p>
                    </div>
                  </div>
                </div>

                {/* Actions Footer */}
                <div className="mt-6 pt-4 border-t border-[#e1e2ed]/60 flex items-center justify-between text-xs">
                  <span className="text-[#737686] text-[11px] font-medium">
                    Created {dept.createdAt ? new Date(dept.createdAt).toLocaleDateString() : 'Active'}
                  </span>
                  <div className="flex items-center gap-2">
                    <button
                      title="Edit Department"
                      onClick={() => setSelectedDeptForEdit(dept)}
                      className="p-1.5 rounded-lg text-[#737686] hover:text-[#004ac6] hover:bg-[#f3f3fe] transition-colors"
                    >
                      <HiOutlinePencilSquare className="w-4 h-4" />
                    </button>
                    <button
                      title="Delete Department"
                      onClick={() => handleDelete(dept.id, dept.name)}
                      className="p-1.5 rounded-lg text-[#737686] hover:text-[#ba1a1a] hover:bg-[#fee2e2] transition-colors"
                    >
                      <HiOutlineTrash className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Modals */}
      <AddDepartmentModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        onAdd={handleCreate}
      />

      <EditDepartmentModal
        isOpen={!!selectedDeptForEdit}
        department={selectedDeptForEdit}
        onClose={() => setSelectedDeptForEdit(null)}
        onUpdate={handleUpdate}
      />
    </div>
  );
};

export default Departments;
