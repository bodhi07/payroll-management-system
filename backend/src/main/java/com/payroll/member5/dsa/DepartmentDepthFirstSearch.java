package com.payroll.member5.dsa;

import com.payroll.member5.entity.Department;
import java.util.*;

/**
 * ============================================================================
 * MEMBER 05 ALGORITHM: Custom Depth-First Search (DFS) & BFS Hierarchy Traversal
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Traverses the department hierarchy to compute cumulative roll-up
 *   metrics (Total Staff in Subtree, Total Sub-department count, Reachability).
 * - Traversal Strategy: Depth-First Search (DFS) using recursion or stack.
 * - Time Complexity: O(V + E) where V = Department nodes, E = Hierarchy links.
 * - Space Complexity: O(V) for visited tracker and recursion call stack.
 */
public class DepartmentDepthFirstSearch {

    public static class SubtreeReport {
        private int totalDepartmentCount;
        private List<Department> allSubDepartments;

        public SubtreeReport() {
            this.totalDepartmentCount = 0;
            this.allSubDepartments = new ArrayList<>();
        }

        public int getTotalDepartmentCount() { return totalDepartmentCount; }
        public List<Department> getAllSubDepartments() { return allSubDepartments; }
    }

    /**
     * Performs DFS starting from a root department to find all nested child departments.
     */
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

    /**
     * Level-Order / Breadth-First Search (BFS) to traverse department hierarchy level-by-level.
     */
    public static List<Department> traverseHierarchyBFS(DepartmentGraph graph, Long rootDeptId) {
        List<Department> result = new ArrayList<>();
        if (graph == null || rootDeptId == null) return result;

        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();

        queue.add(rootDeptId);
        visited.add(rootDeptId);

        while (!queue.isEmpty()) {
            Long currentId = queue.poll();
            Department dept = graph.getDepartment(currentId);
            if (dept != null) {
                result.add(dept);
            }

            for (Long neighborId : graph.getChildDepartmentIds(currentId)) {
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    queue.add(neighborId);
                }
            }
        }

        return result;
    }
}
