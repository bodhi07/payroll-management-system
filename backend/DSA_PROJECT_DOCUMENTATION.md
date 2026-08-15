# Enterprise Payroll Management System - Complete DSA Mapping per Member

This document outlines all **Data Structures** and **Algorithms** organized directly inside each member's directory under `backend/src/main/java/com/payroll/memberX/dsa/`.

---

##  Member 01: Employee Management System
**Package**: `com.payroll.member1.dsa`
- **Data Structures**:
  1. `EmployeeHashTable.java` - Specialized employee separate chaining hash table ($O(1)$ lookup by NIC / EmpNo).
  2. `CustomHashTable.java` - Generic key-value hash table.
  3. `CustomLinkedList.java` - Doubly linked list for employee record traversal.
  4. `CustomArray.java` - Dynamic resizable array list.
- **Algorithms**:
  1. `EmployeeMergeSort.java` - Stably sorts employees by salary or join date ($O(N \log N)$).
  2. `LinearSearch.java` - Multi-field search across employee records ($O(N)$).
  3. `MergeSort.java` - Generic divide-and-conquer merge sort ($O(N \log N)$).
  4. `InsertionSort.java` - In-place insertion sort for small datasets ($O(N^2)$).
- **Viva Guide**: [`MEMBER_01_DSA_GUIDE.md`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member1/dsa/MEMBER_01_DSA_GUIDE.md)

---

##  Member 02: Attendance & Shift Management
**Package**: `com.payroll.member2.dsa`
- **Data Structures**:
  1. `AttendanceCircularQueue.java` - Circular FIFO ring buffer for live shift punch tracking ($O(1)$ enqueue/dequeue).
  2. `CustomQueue.java` - Generic linked node FIFO queue.
- **Algorithms**:
  1. `AttendanceBinarySearch.java` - Date-based shift lookup in logarithmic time ($O(\log N)$).
  2. `BubbleSort.java` - Sequential shift comparison sort ($O(N^2)$).
- **Viva Guide**: [`MEMBER_02_DSA_GUIDE.md`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member2/dsa/MEMBER_02_DSA_GUIDE.md)

---

##  Member 03: Payroll & Statutory Calculation
**Package**: `com.payroll.member3.dsa`
- **Data Structures**:
  1. `PayrollBinarySearchTree.java` - Net salary BST for range queries and min/max lookups ($O(\log N)$).
  2. `CustomBinarySearchTree.java` - Generic binary search tree.
  3. `CustomAVLTree.java` - Self-balancing AVL tree for guaranteed logarithmic search.
- **Algorithms**:
  1. `SalaryQuickSort.java` - Fast in-place quicksort for ranking top earners ($O(N \log N)$).
  2. `QuickSort.java` - Generic Lomuto-partitioned quick sort.
  3. `SelectionSort.java` - In-place minimum selection sort ($O(N^2)$).
- **Viva Guide**: [`MEMBER_03_DSA_GUIDE.md`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member3/dsa/MEMBER_03_DSA_GUIDE.md)

---

##  Member 04: Leave & Time-Off Management
**Package**: `com.payroll.member4.dsa`
- **Data Structures**:
  1. `LeavePriorityQueue.java` - Binary max-heap prioritizing urgent medical leaves ($O(\log N)$).
  2. `CustomSet.java` - Unique date set preventing duplicate leave bookings.
- **Algorithms**:
  1. `LeaveOverlapIntervalAlgorithm.java` - Interval overlap and conflict scheduler ($O(N)$).
- **Viva Guide**: [`MEMBER_04_DSA_GUIDE.md`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member4/dsa/MEMBER_04_DSA_GUIDE.md)

---

##  Member 05: Department & Hierarchy Management
**Package**: `com.payroll.member5.dsa`
- **Data Structures**:
  1. `DepartmentGraph.java` - Corporate organizational hierarchy graph / DAG (Adjacency List).
  2. `CustomGraph.java` - Generic vertex/edge graph.
- **Algorithms**:
  1. `DepartmentDepthFirstSearch.java` - DFS/BFS recursive roll-up metrics for child departments ($O(V + E)$).
- **Viva Guide**: [`MEMBER_05_DSA_GUIDE.md`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member5/dsa/MEMBER_05_DSA_GUIDE.md)

---

##  Member 06: User Administration, RBAC & Security Audit
**Package**: `com.payroll.member6.dsa`
- **Data Structures**:
  1. `AuditStack.java` - LIFO stack for recent security action tracking and rollback ($O(1)$ push/pop).
  2. `CustomStack.java` - Generic linked node LIFO stack.
- **Algorithms**:
  1. `RolePermissionTrie.java` - Prefix tree for $O(L)$ URL route RBAC permission validation.
  2. `AuditLogMergeSort.java` - Stable timestamp sorting for audit trails ($O(N \log N)$).
- **Viva Guide**: [`MEMBER_06_DSA_GUIDE.md`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member6/dsa/MEMBER_06_DSA_GUIDE.md)
