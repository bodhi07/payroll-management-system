# MEMBER 06 - DATA STRUCTURES & ALGORITHMS (DSA) EXPLANATION & CODE GUIDE
**Module**: User Administration, Role-Based Access Control (RBAC) & Security Audit  
**Package**: `com.payroll.member6.dsa`

---

## 1. Custom Data Structure: `AuditStack` (LIFO Dynamic Stack)

###  Why this Data Structure?
In security telemetry and system auditing, administrators need to inspect the **most recent** transactional events first (e.g. recent logins, salary generations, permission changes) or execute rollback/undo operations.
`AuditStack` implements a **Last-In, First-Out (LIFO) Linked Node Stack** providing **$O(1)$ constant time** event push and pop operations.

###  Source Code & Key Methods:
```java
public class AuditStack {
    private static class StackNode {
        AuditLog data;
        StackNode next;
        public StackNode(AuditLog data) {
            this.data = data;
            this.next = null;
        }
    }

    private StackNode top = null;
    private int size = 0;

    // O(1) Push Recent Security Action
    public synchronized void push(AuditLog log) {
        if (log == null) return;
        StackNode newNode = new StackNode(log);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // O(1) Pop Most Recent Action
    public synchronized AuditLog pop() {
        if (isEmpty()) throw new EmptyStackException();
        AuditLog value = top.data;
        top = top.next;
        size--;
        return value;
    }

    // O(1) Peek at Latest Event
    public synchronized AuditLog peek() {
        return isEmpty() ? null : top.data;
    }
}
```

### ⏱ Complexity Analysis:
| Operation | Best Case | Average Case | Worst Case | Space Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **Push (Log Event)** | $O(1)$ | **$O(1)$** | $O(1)$ | $O(N)$ stack nodes |
| **Pop (Recent Event)** | $O(1)$ | **$O(1)$** | $O(1)$ | $O(1)$ |
| **Peek (Latest Action)**| $O(1)$ | **$O(1)$** | $O(1)$ | $O(1)$ |

---

## 2. Custom Algorithm: `RolePermissionTrie` (Prefix Tree RBAC Validator)

###  Why this Algorithm?
When HTTP requests hit the backend, matching endpoint paths (e.g. `/api/v1/payroll/disburse`) against regex patterns repeatedly is computationally expensive.
`RolePermissionTrie` organizes API routes as a **Prefix Tree (Trie)**, validating user security permissions in **$O(L)$ time**, where $L$ is the character length of the URL.

###  Source Code & Key Methods:
```java
public class RolePermissionTrie {
    public static class TrieNode {
        public Map<Character, TrieNode> children = new HashMap<>();
        public boolean isEndOfPermission = false;
        public String requiredRole = null;
    }

    private final TrieNode root = new TrieNode();

    // O(L) Route Insertion
    public void insertPermission(String apiRoute, String requiredRole) {
        if (apiRoute == null || apiRoute.isEmpty()) return;
        TrieNode current = root;
        for (char ch : apiRoute.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfPermission = true;
        current.requiredRole = requiredRole;
    }

    // O(L) Permission Validation
    public boolean hasAccess(String apiRoute, List<String> userRoles) {
        if (apiRoute == null || userRoles == null) return false;
        TrieNode current = root;
        for (char ch : apiRoute.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return true; // Public route
            }
            current = current.children.get(ch);
        }

        if (current.isEndOfPermission && current.requiredRole != null) {
            return userRoles.contains(current.requiredRole) || userRoles.contains("ROLE_ADMIN");
        }
        return true;
    }
}
```

### ⏱ Complexity Analysis:
- **Permission Check Time**: $O(L)$ where $L$ is the URL string length (independent of number of routes).
- **Space Complexity**: $O(\Sigma \cdot L)$ for Trie character transitions.

---

## 🎯 Viva / Presentation Questions & Answers (Member 06)

1. **Q: Why is a Stack suitable for Audit Logs?**
   - **A**: A Stack operates on a LIFO (Last-In, First-Out) basis, making it ideal for retrieving the most recent security events first without sorting.
2. **Q: Why use a Trie for API route security checks?**
   - **A**: A Trie matches route prefixes in $O(L)$ time (where $L$ is URL length), which is significantly faster and more scalable than iterating over a list of routes with regex.
