# MEMBER 05 - DATA STRUCTURES & ALGORITHMS (DSA) GUIDE
**Module**: Department & Organizational Hierarchy System

---

## 1. Custom Data Structure: `DepartmentGraph` (Directed Acyclic Graph / Tree)
- **File**: [`DepartmentGraph.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member5/dsa/DepartmentGraph.java)
- **Concept**: Graph representation using an Adjacency List `Map<Long, List<Long>>` modeling corporate divisions, parent departments, and reporting sub-units.
- **Why this DS?**: Accurately models organizational hierarchy and enables multi-level divisional analysis.
- **Complexity**:
  - `addDepartment()`: **$O(1)$ constant time**.
  - `addHierarchicalLink()`: **$O(1)$ constant time**.
  - Space: **$O(V + E)$** where $V$ = Departments, $E$ = Hierarchy links.

---

## 2. Custom Algorithm: `DepartmentDepthFirstSearch` (DFS & BFS Hierarchy Traversal)
- **File**: [`DepartmentDepthFirstSearch.java`](file:///c:/Users/chath/Desktop/payroll-management-system/backend/src/main/java/com/payroll/member5/dsa/DepartmentDepthFirstSearch.java)
- **Concept**: Recursive Depth-First Search (DFS) and Level-Order Breadth-First Search (BFS) graph traversal.
- **Why this Algorithm?**: Computes cumulative roll-up metrics (total headcounts and combined basic salary budget across all nested sub-departments under a parent division).
- **Complexity**:
  - Time Complexity: **$O(V + E)$**
  - Space Complexity: **$O(V)$** recursion stack / visited set.
