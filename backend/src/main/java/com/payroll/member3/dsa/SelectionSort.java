package com.payroll.member3.dsa;

/**
 * ============================================================================
 * Member 03 Algorithm: Selection Sort
 * ============================================================================
 */
public class SelectionSort {

    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) return;
        final int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j].compareTo(array[minIdx]) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                final T temp = array[minIdx];
                array[minIdx] = array[i];
                array[i] = temp;
            }
        }
    }
}
