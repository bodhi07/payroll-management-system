package com.payroll.member2.dsa;

/**
 * ============================================================================
 * Member 02 Algorithm: Bubble Sort
 * ============================================================================
 */
public class BubbleSort {

    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) return;
        final int n = array.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    final T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
}
