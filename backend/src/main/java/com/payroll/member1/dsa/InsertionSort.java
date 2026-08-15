package com.payroll.member1.dsa;

/**
 * ============================================================================
 * Member 01 Algorithm: Insertion Sort
 * ============================================================================
 */
public class InsertionSort {

    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) return;
        for (int i = 1; i < array.length; i++) {
            final T key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
