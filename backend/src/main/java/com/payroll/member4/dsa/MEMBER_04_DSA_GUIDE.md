# MEMBER 04 - DATA STRUCTURES & ALGORITHMS (DSA) EXPLANATION & CODE GUIDE
**Module**: Leave & Time-Off Management System  
**Package**: `com.payroll.member4.dsa`

---

## 1. Custom Data Structure: `LeavePriorityQueue` (Binary Max-Heap)

###  Why this Data Structure?
When numerous employees submit leave applications, HR managers should not treat critical emergency medical leaves with the same urgency as future vacation leaves.
`LeavePriorityQueue` implements a **Binary Max-Heap** where leaves are prioritized by medical urgency:
- **MEDICAL Leave**: Priority Weight = `300` (Highest Priority)
- **CASUAL Leave**: Priority Weight = `200` (Medium Priority)
- **ANNUAL Leave**: Priority Weight = `100` (Standard Priority)

This ensures emergency medical leave requests automatically float to the top of the queue for immediate HR approval.

###  Source Code & Key Methods:
```java
public class LeavePriorityQueue {
    private static class PrioritizedLeave {
        LeaveRequest leave;
        int priorityWeight;

        public PrioritizedLeave(LeaveRequest leave) {
            this.leave = leave;
            this.priorityWeight = calculateWeight(leave);
        }

        private int calculateWeight(LeaveRequest l) {
            if (l == null || l.getLeaveType() == null) return 0;
            String type = l.getLeaveType().toUpperCase();
            if (type.contains("MEDICAL")) return 300;
            if (type.contains("CASUAL")) return 200;
            if (type.contains("ANNUAL")) return 100;
            return 50;
        }
    }

    private List<PrioritizedLeave> heap = new ArrayList<>();

    // O(log N) Insert & Heapify-Up
    public synchronized void insert(LeaveRequest leave) {
        if (leave == null) return;
        heap.add(new PrioritizedLeave(leave));
        heapifyUp(heap.size() - 1);
    }

    // O(log N) Extract Highest Priority Urgent Leave
    public synchronized LeaveRequest extractMax() {
        if (heap.isEmpty()) return null;
        LeaveRequest maxLeave = heap.get(0).leave;
        PrioritizedLeave last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return maxLeave;
    }

    private void heapifyUp(int index) {
        int parent = (index - 1) / 2;
        while (index > 0 && heap.get(index).priorityWeight > heap.get(parent).priorityWeight) {
            Collections.swap(heap, index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }
}
```

### ⏱ Complexity Analysis:
| Operation | Best Case | Average Case | Worst Case | Space Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **Insert (New Leave)** | $O(1)$ | **$O(\log N)$** | $O(\log N)$ | $O(N)$ heap elements |
| **Extract Max (Review)**| $O(1)$ | **$O(\log N)$** | $O(\log N)$ | $O(1)$ |
| **Peek (Inspect Top)** | $O(1)$ | **$O(1)$** | $O(1)$ | $O(1)$ |

---

## 2. Custom Algorithm: `LeaveOverlapIntervalAlgorithm` (Interval Scheduling)

###  Why this Algorithm?
To prevent an employee from double-booking or applying for overlapping leaves on identical dates, `LeaveOverlapIntervalAlgorithm` mathematically detects interval clashes.

###  Mathematical Definition:
Two date intervals $[S_1, E_1]$ and $[S_2, E_2]$ overlap **if and only if**:
$$\max(S_1, S_2) \le \min(E_1, E_2)$$

###  Source Code & Key Methods:
```java
public class LeaveOverlapIntervalAlgorithm {

    public static class IntervalResult {
        private boolean hasConflict;
        private LeaveRequest conflictingLeave;

        public IntervalResult(boolean hasConflict, LeaveRequest conflictingLeave) {
            this.hasConflict = hasConflict;
            this.conflictingLeave = conflictingLeave;
        }
        public boolean isHasConflict() { return hasConflict; }
        public LeaveRequest getConflictingLeave() { return conflictingLeave; }
    }

    // O(N) Conflict Detection
    public static IntervalResult checkOverlap(List<LeaveRequest> existingLeaves, LocalDate newStart, LocalDate newEnd) {
        if (existingLeaves == null || existingLeaves.isEmpty() || newStart == null || newEnd == null) {
            return new IntervalResult(false, null);
        }

        for (LeaveRequest existing : existingLeaves) {
            LocalDate start = existing.getStartDate();
            LocalDate end = existing.getEndDate();
            if (start == null || end == null) continue;

            // Conflict Condition: !(newStart > end) AND !(newEnd < start)
            boolean overlaps = !newStart.isAfter(end) && !newEnd.isBefore(start);
            if (overlaps) {
                return new IntervalResult(true, existing);
            }
        }
        return new IntervalResult(false, null);
    }
}
```

### ⏱ Complexity Analysis:
- **Single Check Time**: $O(N)$ linear scan against $N$ approved leaves.
- **Sorted Sweep Time**: $O(N \log N)$ sorting + $O(N)$ sweep.
- **Space Complexity**: $O(1)$ auxiliary memory.

---

## 🎯 Viva / Presentation Questions & Answers (Member 04)

1. **Q: Why use a Priority Queue (Heap) instead of a simple FIFO Queue for leaves?**
   - **A**: A simple FIFO Queue processes leaves purely by submission time. A Priority Queue ensures urgent Medical leaves are triaged first ahead of recreational annual leaves.
2. **Q: How does your algorithm detect date overlaps between two leave requests?**
   - **A**: It checks if the new leave's start date is on or before the existing leave's end date, AND the new leave's end date is on or after the existing start date (`!newStart.isAfter(end) && !newEnd.isBefore(start)`).
