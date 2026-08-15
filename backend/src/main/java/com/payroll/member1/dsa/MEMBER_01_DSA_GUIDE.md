# MEMBER 01 - DATA STRUCTURES & ALGORITHMS (DSA) EXPLANATION & CODE GUIDE
**Module**: Employee Management System  
**Package**: `com.payroll.member1.dsa`

---

## 1. Custom Data Structure: `EmployeeHashTable` (Separate Chaining)

###  Why this Data Structure?
In an enterprise HRMS, searching for an employee using their National Identity Card (NIC) or Employee ID in a database table containing tens of thousands of records can cause database I/O bottlenecks. 
`EmployeeHashTable` maintains an in-memory hash table with **$O(1)$ constant-time** lookup, insertion, and deletion.

###  Collision Resolution Technique:
- **Separate Chaining**: Uses an array of LinkedList buckets. If two employee keys produce the same hash index (a collision), they are chained together in that bucket's linked list.

###  Source Code & Key Methods:
```java
public class EmployeeHashTable {
    private static class HashNode {
        String key;          // NIC / Employee Number
        Employee value;      // Full Employee Record Entity
        HashNode next;       // Linked chain for collisions

        public HashNode(String key, Employee value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode[] buckets;
    private int capacity = 16;
    private int size = 0;

    // Polynomial hash function
    private int getBucketIndex(String key) {
        if (key == null) return 0;
        int hashCode = 0;
        for (int i = 0; i < key.length(); i++) {
            hashCode = (31 * hashCode + key.charAt(i)) % capacity;
        }
        return Math.abs(hashCode);
    }

    // O(1) Average Insertion
    public void put(String key, Employee employee) {
        int bucketIndex = getBucketIndex(key);
        HashNode head = buckets[bucketIndex];

        while (head != null) {
            if (head.key.equalsIgnoreCase(key)) {
                head.value = employee; // Update existing
                return;
            }
            head = head.next;
        }

        size++;
        head = buckets[bucketIndex];
        HashNode newNode = new HashNode(key, employee);
        newNode.next = head;
        buckets[bucketIndex] = newNode;
    }

    // O(1) Average Lookup
    public Employee get(String key) {
        int bucketIndex = getBucketIndex(key);
        HashNode head = buckets[bucketIndex];
        while (head != null) {
            if (head.key.equalsIgnoreCase(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }
}
```

### ⏱ Complexity Analysis:
| Operation | Best Case | Average Case | Worst Case | Space Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **Search / Get** | $O(1)$ | **$O(1)$** | $O(N)$ (if all hash into 1 bucket) | $O(1)$ auxiliary |
| **Insert / Put** | $O(1)$ | **$O(1)$** | $O(N)$ | $O(1)$ auxiliary |
| **Delete / Remove**| $O(1)$ | **$O(1)$** | $O(N)$ | $O(1)$ auxiliary |

---

## 2. Custom Algorithm: `EmployeeMergeSort` (Divide & Conquer)

###  Why this Algorithm?
When sorting large employee payroll rosters by **Basic Salary** or **Join Date**, standard algorithms like QuickSort can degrade to $O(N^2)$ if datasets contain already sorted or identical elements. `EmployeeMergeSort` guarantees **$O(N \log N)$** performance under all conditions and is **stable** (preserves original record order for employees with identical salaries).

###  Source Code & Key Methods:
```java
public class EmployeeMergeSort {

    public static List<Employee> sort(List<Employee> employees, SortOrder order) {
        if (employees == null || employees.size() <= 1) return employees;
        List<Employee> list = new ArrayList<>(employees);
        mergeSort(list, 0, list.size() - 1, order);
        return list;
    }

    private static void mergeSort(List<Employee> list, int left, int right, SortOrder order) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid, order);       // Divide left half
            mergeSort(list, mid + 1, right, order);   // Divide right half
            merge(list, left, mid, right, order);     // Conquer & Merge
        }
    }

    private static void merge(List<Employee> list, int left, int mid, int right, SortOrder order) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Employee> L = new ArrayList<>(n1);
        List<Employee> R = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) L.add(list.get(left + i));
        for (int j = 0; j < n2; ++j) R.add(list.get(mid + 1 + j));

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (compare(L.get(i), R.get(j), order) <= 0) {
                list.set(k, L.get(i));
                i++;
            } else {
                list.set(k, R.get(j));
                j++;
            }
            k++;
        }
        while (i < n1) list.set(k++, L.get(i++));
        while (j < n2) list.set(k++, R.get(j++));
    }
}
```

### ⏱ Complexity Analysis:
- **Best Case Time**: $O(N \log N)$
- **Average Case Time**: $O(N \log N)$
- **Worst Case Time**: $O(N \log N)$
- **Space Complexity**: $O(N)$ auxiliary memory for merging.

---

## 🎯 Viva / Presentation Questions & Answers (Member 01)

1. **Q: Why did you choose a Hash Table for Employee lookup instead of a Binary Search Tree?**
   - **A**: A Hash Table provides $O(1)$ constant average lookup time using the employee's unique NIC or ID, whereas a BST requires $O(\log N)$ comparisons.
2. **Q: How does your Hash Table handle collisions?**
   - **A**: It uses **Separate Chaining** where each bucket in the array holds a linked list of entries that hash to that same index.
3. **Q: Why MergeSort over QuickSort for employees?**
   - **A**: MergeSort is **stable**, meaning employees with the exact same salary will maintain their relative original join order, and it guarantees $O(N \log N)$ worst-case time.
