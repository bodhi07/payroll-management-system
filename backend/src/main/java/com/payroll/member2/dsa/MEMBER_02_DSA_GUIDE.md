# MEMBER 02 - DATA STRUCTURES & ALGORITHMS (DSA) GUIDE
**Module**: Attendance & Shift Management System

---

## 1. Custom Data Structure: `AttendanceCircularQueue` (FIFO Ring Buffer)
- **File**: [`AttendanceCircularQueue.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member2/dsa/AttendanceCircularQueue.java)
- **Concept**: First-In, First-Out (FIFO) queue utilizing a circular array buffer with head and tail pointers.
- **Why this DS?**: Ensures employee daily check-in punch logs are processed in exact arrival sequence without memory reallocation overhead or array shifts.
- **Complexity**:
  - `enqueue()` (Record Shift Punch): **$O(1)$ constant time**.
  - `dequeue()` (Process Shift): **$O(1)$ constant time**.
  - `peek()`: **$O(1)$ constant time**.
  - Space: **$O(N)$** linear space.

---

## 2. Custom Algorithm: `AttendanceBinarySearch` (Divide & Conquer)
- **File**: [`AttendanceBinarySearch.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member2/dsa/AttendanceBinarySearch.java)
- **Concept**: Binary Search algorithm searching across chronologically sorted shift logs.
- **Why this Algorithm?**: Instantly retrieves an employee's check-in/check-out shift record for any specific date in logarithmic time rather than traversing the entire yearly shift history.
- **Complexity**:
  - Best Case: **$O(1)$**
  - Average Case: **$O(\log N)$**
  - Worst Case: **$O(\log N)$**
  - Space Complexity: **$O(1)$** iterative memory.
