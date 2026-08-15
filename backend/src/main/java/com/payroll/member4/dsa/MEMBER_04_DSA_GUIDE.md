# MEMBER 04 - DATA STRUCTURES & ALGORITHMS (DSA) GUIDE
**Module**: Leave & Time-Off Management System

---

## 1. Custom Data Structure: `LeavePriorityQueue` (Binary Max-Heap)
- **File**: [`LeavePriorityQueue.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member4/dsa/LeavePriorityQueue.java)
- **Concept**: Binary Max-Heap prioritizing elements based on urgency weights (`MEDICAL: 300` > `CASUAL: 200` > `ANNUAL: 100`).
- **Why this DS?**: Automatically bubbles urgent medical leave requests to the top of the HR pending review queue so critical employee emergencies are addressed first.
- **Complexity**:
  - `insert()` (New Application): **$O(\log N)$** heapify-up.
  - `extractMax()` (Process Top Urgency): **$O(\log N)$** heapify-down.
  - `peek()` (Inspect Highest Priority): **$O(1)$ constant time**.
  - Space: **$O(N)$**.

---

## 2. Custom Algorithm: `LeaveOverlapIntervalAlgorithm` (Interval Scheduling)
- **File**: [`LeaveOverlapIntervalAlgorithm.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member4/dsa/LeaveOverlapIntervalAlgorithm.java)
- **Concept**: Interval Scheduling overlap detection using temporal interval boundaries.
- **Why this Algorithm?**: Mathematical overlap validation ensuring no employee can apply for overlapping leaves on identical dates (`max(S1, S2) <= min(E1, E2)`).
- **Complexity**:
  - Single Interval Verification: **$O(N)$ linear time**.
  - Chronological Interval Sorting: **$O(N \log N)$**.
  - Space Complexity: **$O(1)$**.
