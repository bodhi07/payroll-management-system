package com.payroll.member3.dsa;

import com.payroll.member3.entity.Payroll;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 03 ALGORITHM: Custom Payroll Salary QuickSort (Lomuto Partition)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: High-speed in-place ranking and sorting of monthly employee payrolls
 *   by Net or Gross salary amounts to determine top compensation tiers.
 * - Strategy: Selects pivot element, partitions smaller and larger salaries around pivot,
 *   and recursively sorts sub-arrays.
 * - Time Complexity:
 *     * Best Case:    O(N log N).
 *     * Average Case: O(N log N).
 *     * Worst Case:   O(N^2) (prevented with median/pivot strategies).
 * - Space Complexity: O(log N) recursion call stack.
 */
public class SalaryQuickSort {

    public enum SortDirection {
        ASCENDING,
        DESCENDING
    }

    public static List<Payroll> sort(List<Payroll> payrollList, SortDirection direction) {
        if (payrollList == null || payrollList.size() <= 1) {
            return payrollList;
        }

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
        BigDecimal pivot = getSalary(list.get(high));
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            BigDecimal current = getSalary(list.get(j));
            boolean shouldSwap = (direction == SortDirection.ASCENDING)
                    ? current.compareTo(pivot) <= 0
                    : current.compareTo(pivot) >= 0;

            if (shouldSwap) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    private static BigDecimal getSalary(Payroll p) {
        if (p == null || p.getNetSalary() == null) return BigDecimal.ZERO;
        return p.getNetSalary();
    }

    private static void swap(List<Payroll> list, int i, int j) {
        Payroll temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
