# MEMBER 02 - DATA STRUCTURES & ALGORITHMS (DSA) EXPLANATION & CODE GUIDE
**Module**: Attendance & Shift Management System  
**Package**: `com.payroll.member2.dsa`

---

## 1. Custom Data Structure: `AttendanceCircularQueue` (FIFO Ring Buffer)

###  Why this Data Structure?
During morning and evening shift changeovers, thousands of employee check-in biometric punch events arrive rapidly. 
`AttendanceCircularQueue` uses a fixed-memory **Circular Array Ring Buffer** with front/rear pointers, ensuring First-In, First-Out (FIFO) processing with **$O(1)$ constant time** enqueue and dequeue without costly array shifting or memory reallocation.

###  Source Code & Key Methods:
```java
public class AttendanceCircularQueue {
    private Attendance[] queue;
    private int front = 0;
    private int rear = -1;
    private int size = 0;
    private int capacity;

    public AttendanceCircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new Attendance[capacity];
    }

    // O(1) Shift Check-In Enqueue
    public synchronized boolean enqueue(Attendance record) {
        if (size == capacity) {
            expandCapacity();
        }
        rear = (rear + 1) % capacity; // Circular wrap-around
        queue[rear] = record;
        size++;
        return true;
    }

    // O(1) FIFO Dequeue for processing
    public synchronized Attendance dequeue() {
        if (isEmpty()) return null;
        Attendance item = queue[front];
        queue[front] = null;
        front = (front + 1) % capacity; // Circular advance
        size--;
        return item;
    }
}
```

### ⏱ Complexity Analysis:
| Operation | Best Case | Average Case | Worst Case | Space Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **Enqueue (Check-in)** | $O(1)$ | **$O(1)$** | $O(1)$ amortized | $O(N)$ buffer capacity |
| **Dequeue (Process)** | $O(1)$ | **$O(1)$** | $O(1)$ | $O(1)$ |
| **Peek (Top Shift)** | $O(1)$ | **$O(1)$** | $O(1)$ | $O(1)$ |

---

## 2. Custom Algorithm: `AttendanceBinarySearch` (Divide & Conquer)

###  Why this Algorithm?
To look up whether an employee was present or absent on a specific date (e.g. `2026-08-15`) out of hundreds of historical shift records, a linear scan would take $O(N)$ time. `AttendanceBinarySearch` searches across date-sorted logs in **$O(\log N)$ logarithmic time**.

###  Source Code & Key Methods:
```java
public class AttendanceBinarySearch {

    // O(log N) Date Lookup Algorithm
    public static int searchByDate(List<Attendance> sortedList, LocalDate targetDate) {
        if (sortedList == null || sortedList.isEmpty() || targetDate == null) {
            return -1;
        }

        int low = 0;
        int high = sortedList.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            LocalDate midDate = sortedList.get(mid).getDate();

            int comparison = midDate.compareTo(targetDate);
            if (comparison == 0) {
                return mid; // Found match at index mid
            } else if (comparison < 0) {
                low = mid + 1; // Target is in right sub-array
            } else {
                high = mid - 1; // Target is in left sub-array
            }
        }
        return -1; // Date not found
    }
}
```

### ⏱ Complexity Analysis:
- **Best Case Time**: $O(1)$ (Target date is at the exact center index).
- **Average Case Time**: $O(\log N)$
- **Worst Case Time**: $O(\log N)$
- **Space Complexity**: $O(1)$ iterative memory.

---

## 🎯 Viva / Presentation Questions & Answers (Member 02)

1. **Q: Why is a Circular Queue better than a normal Array Queue for attendance?**
   - **A**: In a regular array queue, dequeuing elements leaves empty unused spaces at the front that require shifting all elements ($O(N)$). A Circular Queue wraps around using modulo arithmetic (`(rear + 1) % capacity`), keeping all operations strictly $O(1)$.
2. **Q: What is the precondition for Binary Search in attendance logs?**
   - **A**: The attendance list must be pre-sorted chronologically by `date` before calling Binary Search.
