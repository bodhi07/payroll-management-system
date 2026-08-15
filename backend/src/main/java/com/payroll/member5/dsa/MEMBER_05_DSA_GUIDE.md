# MEMBER 05 - DATA STRUCTURES & ALGORITHMS (DSA) EXPLANATION & CODE GUIDE
**Module**: Department & Organizational Hierarchy System  
**Package**: `com.payroll.member5.dsa`

---

## 1. Custom Data Structure: `DepartmentGraph` (Directed Acyclic Graph / Tree)

###  Why this Data Structure?
Enterprise corporate organizations have multi-tier divisional reporting lines (e.g. `Executive Management -> Engineering Division -> Backend Core Team -> Cloud Ops`).
`DepartmentGraph` implements an **Adjacency List Directed Graph (DAG)** to model hierarchical relationships, parent divisions, and reporting sub-units.

###  Source Code & Key Methods:
```java
public class DepartmentGraph {
    private Map<Long, Department> departmentNodes = new HashMap<>();
    private Map<Long, List<Long>> adjacencyList = new HashMap<>();

    // O(1) Add Department Node
    public void addDepartment(Department department) {
        if (department == null || department.getId() == null) return;
        departmentNodes.put(department.getId(), department);
        adjacencyList.putIfAbsent(department.getId(), new ArrayList<>());
    }

    // O(1) Add Hierarchical Reporting Link
    public void addHierarchicalLink(Long parentDeptId, Long childDeptId) {
        if (parentDeptId == null || childDeptId == null) return;
        adjacencyList.putIfAbsent(parentDeptId, new ArrayList<>());
        if (!adjacencyList.get(parentDeptId).contains(childDeptId)) {
            adjacencyList.get(parentDeptId).add(childDeptId);
        }
    }

    public List<Long> getChildDepartmentIds(Long parentDeptId) {
        return adjacencyList.getOrDefault(parentDeptId, Collections.emptyList());
    }
}
```

### ⏱ Complexity Analysis:
| Operation | Time Complexity | Space Complexity |
| :--- | :--- | :--- |
| **Add Department Node** | **$O(1)$** | $O(V)$ where $V$ is number of departments |
| **Add Hierarchy Link** | **$O(1)$** | $O(E)$ where $E$ is number of child links |
| **Get Sub-Units** | **$O(1)$** | $O(k)$ children |

---

## 2. Custom Algorithm: `DepartmentDepthFirstSearch` (DFS Hierarchy Traversal)

###  Why this Algorithm?
When calculating the combined headcount and total basic salary budget across an entire division, `DepartmentDepthFirstSearch` uses **Depth-First Search (DFS)** to traverse all nested child sub-departments recursively in $O(V + E)$ time.

###  Source Code & Key Methods:
```java
public class DepartmentDepthFirstSearch {

    public static class SubtreeReport {
        private int totalDepartmentCount = 0;
        private List<Department> allSubDepartments = new ArrayList<>();
        public int getTotalDepartmentCount() { return totalDepartmentCount; }
        public List<Department> getAllSubDepartments() { return allSubDepartments; }
    }

    // O(V + E) DFS Recursive Traversal
    public static SubtreeReport getSubtreeDepartmentsDFS(DepartmentGraph graph, Long rootDeptId) {
        SubtreeReport report = new SubtreeReport();
        if (graph == null || rootDeptId == null) return report;
        Set<Long> visited = new HashSet<>();
        dfsRecursive(graph, rootDeptId, visited, report);
        return report;
    }

    private static void dfsRecursive(DepartmentGraph graph, Long currentId, Set<Long> visited, SubtreeReport report) {
        visited.add(currentId);
        Department dept = graph.getDepartment(currentId);
        if (dept != null) {
            report.allSubDepartments.add(dept);
            report.totalDepartmentCount++;
        }

        for (Long childId : graph.getChildDepartmentIds(currentId)) {
            if (!visited.contains(childId)) {
                dfsRecursive(graph, childId, visited, report);
            }
        }
    }
}
```

### ⏱ Complexity Analysis:
- **Time Complexity**: $O(V + E)$ where $V$ = Department vertices, $E$ = Hierarchy links.
- **Space Complexity**: $O(V)$ recursion call stack and visited tracker set.

---

## 🎯 Viva / Presentation Questions & Answers (Member 05)

1. **Q: Why use a Graph/Adjacency List instead of a flat list for departments?**
   - **A**: A flat list cannot easily represent parent-child organizational hierarchy. A Graph accurately models multiple levels of sub-divisions and reporting trees.
2. **Q: How does DFS calculate roll-up metrics for parent departments?**
   - **A**: DFS recursively visits each child department down the branch, aggregating headcount and budgets back up to the parent division.
