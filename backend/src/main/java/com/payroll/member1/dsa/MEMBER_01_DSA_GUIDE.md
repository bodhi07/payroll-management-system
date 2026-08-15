# MEMBER 01 - DATA STRUCTURES & ALGORITHMS (DSA) GUIDE
**Module**: Employee Management System

---

## 1. Custom Data Structure: `EmployeeHashTable` (Separate Chaining)
- **File**: [`EmployeeHashTable.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member1/dsa/EmployeeHashTable.java)
- **Concept**: Hash Table with polynomial hash code and linked list collision resolution.
- **Why this DS?**: Enables instant $O(1)$ constant-time lookup and retrieval of Employee records by unique National Identity Card (NIC) or Employee Identification Number without scanning the entire database.
- **Complexity**:
  - Insertion: **$O(1)$ average**, $O(N)$ worst-case.
  - Search / Lookup: **$O(1)$ average**.
  - Deletion: **$O(1)$ average**.
  - Space: **$O(N + M)$** where $N$ is total employees, $M$ is bucket capacity.

---

## 2. Custom Algorithm: `EmployeeMergeSort` (Divide & Conquer)
- **File**: [`EmployeeMergeSort.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member1/dsa/EmployeeMergeSort.java)
- **Concept**: Divide and Conquer sorting strategy with recursive sub-array splitting and stable merging.
- **Why this Algorithm?**: Stably sorts employee records by Basic Salary (ascending/descending) or Join Date with guaranteed $O(N \log N)$ worst-case performance, preventing degraded responsiveness during large roster processing.
- **Complexity**:
  - Best Case: **$O(N \log N)$**
  - Average Case: **$O(N \log N)$**
  - Worst Case: **$O(N \log N)$**
  - Space Complexity: **$O(N)$** auxiliary space.
  - Stability: **Stable** (preserves relative order for records with equal salary).
