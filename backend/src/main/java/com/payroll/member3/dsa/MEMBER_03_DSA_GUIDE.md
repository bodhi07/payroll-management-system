# MEMBER 03 - DATA STRUCTURES & ALGORITHMS (DSA) EXPLANATION & CODE GUIDE
**Module**: Payroll & Statutory Calculation System  
**Package**: `com.payroll.member3.dsa`

---

## 1. Custom Data Structure: `PayrollBinarySearchTree` (BST)

###  Why this Data Structure?
In payroll analytics, finance managers frequently need to:
1. Find all employees earning within a specific salary bracket (e.g. `[Rs. 100,000, Rs. 250,000]` for tax/EPF tier auditing).
2. Retrieve the lowest (Min) and highest (Max) executive salary disbursements.
`PayrollBinarySearchTree` indexes monthly payslips by Net Salary, allowing **$O(\log N)$** range filtering and $O(N)$ in-order sorted reporting.

###  Source Code & Key Methods:
```java
public class PayrollBinarySearchTree {
    public static class BSTNode {
        public BigDecimal key; // Net Salary value
        public List<Payroll> payrollRecords; // Multi-record bucket for duplicate salaries
        public BSTNode left, right;

        public BSTNode(Payroll payroll) {
            this.key = payroll.getNetSalary() != null ? payroll.getNetSalary() : BigDecimal.ZERO;
            this.payrollRecords = new ArrayList<>();
            this.payrollRecords.add(payroll);
        }
    }

    private BSTNode root;

    // O(log N) Average Insert
    public void insert(Payroll payroll) {
        BigDecimal net = payroll.getNetSalary() != null ? payroll.getNetSalary() : BigDecimal.ZERO;
        root = insertRec(root, payroll, net);
    }

    private BSTNode insertRec(BSTNode current, Payroll payroll, BigDecimal net) {
        if (current == null) return new BSTNode(payroll);
        int cmp = net.compareTo(current.key);
        if (cmp < 0) current.left = insertRec(current.left, payroll, net);
        else if (cmp > 0) current.right = insertRec(current.right, payroll, net);
        else current.payrollRecords.add(payroll); // Same salary duplicate
        return current;
    }

    // O(log N + K) Salary Range Query [min, max]
    public List<Payroll> findRange(BigDecimal min, BigDecimal max) {
        List<Payroll> result = new ArrayList<>();
        findRangeRec(root, min, max, result);
        return result;
    }

    private void findRangeRec(BSTNode node, BigDecimal min, BigDecimal max, List<Payroll> result) {
        if (node == null) return;
        if (min.compareTo(node.key) < 0) findRangeRec(node.left, min, max, result);
        if (min.compareTo(node.key) <= 0 && max.compareTo(node.key) >= 0) result.addAll(node.payrollRecords);
        if (max.compareTo(node.key) > 0) findRangeRec(node.right, min, max, result);
    }
}
```

### ⏱ Complexity Analysis:
| Operation | Best Case | Average Case | Worst Case | Space Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **Insert** | $O(1)$ | **$O(\log N)$** | $O(N)$ | $O(N)$ tree nodes |
| **Search / Range Query** | $O(1)$ | **$O(\log N)$** | $O(N)$ | $O(K)$ matching items |
| **Min / Max Salary** | $O(1)$ | **$O(\log N)$** | $O(N)$ | $O(1)$ |

---

## 2. Custom Algorithm: `SalaryQuickSort` (Lomuto Partitioning)

###  Why this Algorithm?
`SalaryQuickSort` provides high-speed in-place ranking of monthly payouts from highest to lowest earner without consuming additional auxiliary memory buffers.

###  Source Code & Key Methods:
```java
public class SalaryQuickSort {

    public static List<Payroll> sort(List<Payroll> payrollList, SortDirection direction) {
        if (payrollList == null || payrollList.size() <= 1) return payrollList;
        List<Payroll> list = new ArrayList<>(payrollList);
        quickSort(list, 0, list.size() - 1, direction);
        return list;
    }

    private static void quickSort(List<Payroll> list, int low, int high, SortDirection direction) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, direction);
            quickSort(list, low, pivotIndex - 1, direction);
            quickSort(list, pivotIndex + 1, high, direction);
        }
    }

    private static int partition(List<Payroll> list, int low, int high, SortDirection direction) {
        BigDecimal pivot = list.get(high).getNetSalary();
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            BigDecimal current = list.get(j).getNetSalary();
            boolean shouldSwap = (direction == SortDirection.ASCENDING)
                    ? current.compareTo(pivot) <= 0
                    : current.compareTo(pivot) >= 0;

            if (shouldSwap) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
}
```

### ⏱ Complexity Analysis:
- **Best Case Time**: $O(N \log N)$
- **Average Case Time**: $O(N \log N)$
- **Worst Case Time**: $O(N^2)$ (mitigated by balanced pivot selection)
- **Space Complexity**: $O(\log N)$ call stack recursion.

---

## 🎯 Viva / Presentation Questions & Answers (Member 03)

1. **Q: How does a BST help in payroll range queries?**
   - **A**: If the root salary is smaller than `min`, we can discard the entire left subtree; if it's larger than `max`, we discard the right subtree. This gives an $O(\log N)$ query speed.
2. **Q: Why is QuickSort used for sorting salaries instead of BubbleSort?**
   - **A**: QuickSort operates in $O(N \log N)$ average time compared to BubbleSort's $O(N^2)$, making it suitable for processing thousands of payroll records in milliseconds.
