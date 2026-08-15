# Enterprise Payroll & Human Resource Management System (HRMS)
**Full-Stack Application with Spring Boot 3, React 19, TailwindCSS & Custom DSA**

---

## 📌 Project Architecture Overview
GlobalPay HRMS is a production-grade, modular Enterprise Payroll & HR Management application built with a **Spring Boot 3 (Java 21)** REST API backend, **React + Vite** frontend, and custom **Data Structures & Algorithms (DSA)** integrated per team member.

```
payroll-management-system/
├── backend/
│   ├── src/main/java/com/payroll/
│   │   ├── member1/ (Employee Management & Hash Table / MergeSort DSA)
│   │   ├── member2/ (Attendance & Circular Queue / Binary Search DSA)
│   │   ├── member3/ (Payroll Calculation & BST / QuickSort DSA)
│   │   ├── member4/ (Leave Management & Priority Queue / Interval DSA)
│   │   ├── member5/ (Department Hierarchy & Graph / DFS Traversal DSA)
│   │   └── member6/ (User RBAC, Audit Logs & Stack / Trie DSA)
│   └── DSA_PROJECT_DOCUMENTATION.md
└── frontend/
    └── src/ (React 19, Tailwind CSS, TanStack Query, Axios, Lucide/Heroicons)
```

---

## 👥 Team Member Modules & Custom DSA Matrix

| Member | Module Name | Custom Data Structure (with Code) | Custom Algorithm (with Code) | Viva Guide Link |
| :--- | :--- | :--- | :--- | :--- |
| **Member 01** | **Employee Management** | **`EmployeeHashTable`** ($O(1)$ NIC Lookup via Separate Chaining) | **`EmployeeMergeSort`** ($O(N \log N)$ Stable Salary Sort) | [Member 01 Guide](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member1/dsa/MEMBER_01_DSA_GUIDE.md) |
| **Member 02** | **Attendance & Shifts** | **`AttendanceCircularQueue`** ($O(1)$ FIFO Ring Buffer) | **`AttendanceBinarySearch`** ($O(\log N)$ Shift Date Search) | [Member 02 Guide](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member2/dsa/MEMBER_02_DSA_GUIDE.md) |
| **Member 03** | **Payroll Calculations** | **`PayrollBinarySearchTree`** ($O(\log N)$ Salary BST Range Queries) | **`SalaryQuickSort`** ($O(N \log N)$ Lomuto Earner Ranking) | [Member 03 Guide](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member3/dsa/MEMBER_03_DSA_GUIDE.md) |
| **Member 04** | **Leave Management** | **`LeavePriorityQueue`** ($O(\log N)$ Max-Heap for Medical Leaves) | **`LeaveOverlapIntervalAlgorithm`** ($O(N)$ Overlap Detection) | [Member 04 Guide](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member4/dsa/MEMBER_04_DSA_GUIDE.md) |
| **Member 05** | **Department Hierarchy** | **`DepartmentGraph`** ($O(1)$ Adjacency List Org Tree) | **`DepartmentDepthFirstSearch`** ($O(V+E)$ Divisional DFS Roll-up) | [Member 05 Guide](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member5/dsa/MEMBER_05_DSA_GUIDE.md) |
| **Member 06** | **User RBAC & Audit** | **`AuditStack`** ($O(1)$ LIFO Security Action Stack) | **`RolePermissionTrie`** ($O(L)$ Route Permission Prefix Tree) | [Member 06 Guide](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member6/dsa/MEMBER_06_DSA_GUIDE.md) |

---

## 🚀 How to Run the Project Locally

### 1. Start Backend (Spring Boot 3)
```powershell
cd backend
./mvnw.cmd spring-boot:run
```
- API Base URL: `http://localhost:8080/api/v1`
- Swagger UI Documentation: `http://localhost:8080/swagger-ui.html`

### 2. Start Frontend (React + Vite)
```powershell
cd frontend
npm install
npm run dev
```
- Frontend Web App: `http://localhost:3000` (or `http://localhost:3001`)

---

## 🔑 Test Login Credentials (Pre-Seeded)

| Role | Username | Password | Privileges |
| :--- | :--- | :--- | :--- |
| **Admin / HR Specialist** | `agrani` | `pass1234` | Full access to all 6 member modules, user RBAC & audit logs |
| **Standard Employee** | `john.doe` | `pass1234` | Self-service attendance check-in, leave requests, payslip view |
