# MEMBER 03 - DATA STRUCTURES & ALGORITHMS (DSA) GUIDE
**Module**: Payroll & Statutory Calculation System

---

## 1. Custom Data Structure: `PayrollBinarySearchTree` (BST)
- **File**: [`PayrollBinarySearchTree.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member3/dsa/PayrollBinarySearchTree.java)
- **Concept**: Binary Search Tree ordered by Net Salary values with linked duplicate chains.
- **Why this DS?**: Enables fast range queries `[minSalary, maxSalary]` to filter executive vs. junior employee compensation tiers and allows in-order traversal to retrieve sorted salary ledgers in $O(N)$ time.
- **Complexity**:
  - Insertion: **$O(\log N)$ average**, $O(N)$ worst-case.
  - Search / Range Query: **$O(\log N)$ average**.
  - In-Order Traversal (Sorted Output): **$O(N)$ linear time**.
  - Space: **$O(N)$**.

---

## 2. Custom Algorithm: `SalaryQuickSort` (Lomuto In-Place Partitioning)
- **File**: [`SalaryQuickSort.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member3/dsa/SalaryQuickSort.java)
- **Concept**: In-place QuickSort algorithm with recursive partitioning around pivot salary values.
- **Why this Algorithm?**: High-speed sorting and ranking of monthly payroll disbursements from highest to lowest earner for organizational budget analysis.
- **Complexity**:
  - Best Case: **$O(N \log N)$**
  - Average Case: **$O(N \log N)$**
  - Worst Case: **$O(N^2)$**
  - Space Complexity: **$O(\log N)$** recursion stack.
