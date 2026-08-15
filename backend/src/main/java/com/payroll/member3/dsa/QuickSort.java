package com.payroll.member3.dsa;

/**
 * ============================================================================
 * Member 03 Algorithm: Generic Quick Sort
 * ============================================================================
 */
public class QuickSort {

    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) return;
        quickSort(array, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> void quickSort(final T[] array, final int low, final int high) {
        if (low < high) {
            final int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private static <T extends Comparable<T>> int partition(final T[] array, final int low, final int high) {
        final T pivot = array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                final T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        final T temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }
}
