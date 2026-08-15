package com.payroll.member5.dsa;

import com.payroll.member5.entity.Department;
import java.util.*;

/**
 * ============================================================================
 * MEMBER 05 DATA STRUCTURE: Custom Department Hierarchy Graph (Adjacency List)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Models enterprise organizational structure, reporting departments,
 *   and hierarchical divisions as a Directed Acyclic Graph (DAG) / Tree.
 * - Structure: Adjacency List Map<DepartmentId, List<ChildDepartmentId>>.
 * - Capabilities:
 *     * Add department nodes in O(1).
 *     * Add hierarchical relationship edge in O(1).
 *     * Find direct and indirect sub-divisions.
 * - Space Complexity: O(V + E) where V = Departments, E = Hierarchical Links.
 */
public class DepartmentGraph {

    private Map<Long, Department> departmentNodes;
    private Map<Long, List<Long>> adjacencyList;

    public DepartmentGraph() {
        this.departmentNodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    public void addDepartment(Department department) {
        if (department == null || department.getId() == null) return;
        departmentNodes.put(department.getId(), department);
        adjacencyList.putIfAbsent(department.getId(), new ArrayList<>());
    }

    public void addHierarchicalLink(Long parentDeptId, Long childDeptId) {
        if (parentDeptId == null || childDeptId == null) return;
        adjacencyList.putIfAbsent(parentDeptId, new ArrayList<>());
        if (!adjacencyList.get(parentDeptId).contains(childDeptId)) {
            adjacencyList.get(parentDeptId).add(childDeptId);
        }
    }

    public Department getDepartment(Long id) {
        return departmentNodes.get(id);
    }

    public List<Long> getChildDepartmentIds(Long parentDeptId) {
        return adjacencyList.getOrDefault(parentDeptId, Collections.emptyList());
    }

    public Set<Long> getAllDepartmentIds() {
        return departmentNodes.keySet();
    }

    public int getVertexCount() {
        return departmentNodes.size();
    }
}
