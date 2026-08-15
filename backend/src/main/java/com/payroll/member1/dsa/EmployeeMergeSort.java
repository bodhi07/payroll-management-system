package com.payroll.member1.dsa;

import com.payroll.member1.entity.Employee;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 01 ALGORITHM: Custom Employee Merge Sort (Divide and Conquer)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Stably sorts employee records by Basic Salary (ascending/descending)
 *   or Join Date with guaranteed O(N log N) time complexity.
 * - Strategy: Divide and Conquer. Recursively splits array into halves,
 *   sorts each half, and merges the sorted halves back together.
 * - Time Complexity:
 *     * Best Case:    O(N log N)
 *     * Average Case: O(N log N)
 *     * Worst Case:   O(N log N)
 * - Space Complexity: O(N) auxiliary space.
 * - Stability: Stable (preserves original order of employees with equal salary).
 */
public class EmployeeMergeSort {

    public enum SortOrder {
        SALARY_ASC,
        SALARY_DESC,
        JOIN_DATE_ASC,
        JOIN_DATE_DESC
    }

    /**
     * Main entry method to sort a list of employees.
     */
    public static List<Employee> sort(List<Employee> employees, SortOrder order) {
        if (employees == null || employees.size() <= 1) {
            return employees;
        }

        List<Employee> list = new ArrayList<>(employees);
        mergeSort(list, 0, list.size() - 1, order);
        return list;
    }

    private static void mergeSort(List<Employee> list, int left, int right, SortOrder order) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Divide phase: recursively sort left and right halves
            mergeSort(list, left, mid, order);
            mergeSort(list, mid + 1, right, order);

            // Conquer & Combine phase: merge sorted halves
            merge(list, left, mid, right, order);
        }
    }

    private static void merge(List<Employee> list, int left, int mid, int right, SortOrder order) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Employee> leftList = new ArrayList<>(n1);
        List<Employee> rightList = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) leftList.add(list.get(left + i));
        for (int j = 0; j < n2; ++j) rightList.add(list.get(mid + 1 + j));

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (compare(leftList.get(i), rightList.get(j), order) <= 0) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }

    private static int compare(Employee e1, Employee e2, SortOrder order) {
        switch (order) {
            case SALARY_ASC:
                return Double.compare(
                        e1.getBasicSalary() != null ? e1.getBasicSalary().doubleValue() : 0.0,
                        e2.getBasicSalary() != null ? e2.getBasicSalary().doubleValue() : 0.0
                );
            case SALARY_DESC:
                return Double.compare(
                        e2.getBasicSalary() != null ? e2.getBasicSalary().doubleValue() : 0.0,
                        e1.getBasicSalary() != null ? e1.getBasicSalary().doubleValue() : 0.0
                );
            case JOIN_DATE_ASC:
                if (e1.getJoinDate() == null) return 1;
                if (e2.getJoinDate() == null) return -1;
                return e1.getJoinDate().compareTo(e2.getJoinDate());
            case JOIN_DATE_DESC:
                if (e1.getJoinDate() == null) return 1;
                if (e2.getJoinDate() == null) return -1;
                return e2.getJoinDate().compareTo(e1.getJoinDate());
            default:
                return 0;
        }
    }
}
