# MEMBER 06 - DATA STRUCTURES & ALGORITHMS (DSA) GUIDE
**Module**: User Administration, Role-Based Access Control (RBAC) & Security Audit

---

## 1. Custom Data Structure: `AuditStack` (LIFO Dynamic Stack)
- **File**: [`AuditStack.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member6/dsa/AuditStack.java)
- **Concept**: Last-In, First-Out (LIFO) Linked Node Stack.
- **Why this DS?**: Tracks recent sensitive security events in reverse chronological order for instant dashboard activity streaming and administrative rollback history.
- **Complexity**:
  - `push()` (Record Audit Event): **$O(1)$ constant time**.
  - `pop()` (Retrieve Recent Event): **$O(1)$ constant time**.
  - `peek()` (Inspect Latest Action): **$O(1)$ constant time**.
  - Space: **$O(N)$**.

---

## 2. Custom Algorithm: `RolePermissionTrie` & `AuditLogMergeSort`
- **File**: [`RolePermissionTrie.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member6/dsa/RolePermissionTrie.java)
- **Concept**: Prefix Tree (Trie) for endpoint route matching and Divide-and-Conquer MergeSort for audit timestamps.
- **Why these Algorithms?**:
  1. **Trie Route Validator**: Evaluates API endpoint route permissions in $O(L)$ time where $L$ is URL character length, ensuring instant RBAC checking before controller dispatch.
  2. **Audit Timestamp MergeSort**: Stably sorts millions of audit log records chronologically with guaranteed $O(N \log N)$ worst-case performance.
- **Complexity**:
  - Route Permission Check: **$O(L)$** where $L$ is route length.
  - Audit Log Sort: **$O(N \log N)$**.
  - Space Complexity: **$O(N)$**.
