package com.payroll.member1.dsa;

import java.util.Arrays;

/**
 * ============================================================================
 * Member 01 Algorithm: Generic Merge Sort
 * ============================================================================
 */
public class MergeSort {

    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) return;
        final T[] helper = Arrays.copyOf(array, array.length);
        mergeSort(array, helper, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> void mergeSort(final T[] array, final T[] helper, final int low, final int high) {
        if (low < high) {
            final int mid = low + (high - low) / 2;
            mergeSort(array, helper, low, mid);
            mergeSort(array, helper, mid + 1, high);
            merge(array, helper, low, mid, high);
        }
    }

    private static <T extends Comparable<T>> void merge(final T[] array, final T[] helper, final int low, final int mid, final int high) {
        System.arraycopy(array, low, helper, low, high - low + 1);

        int i = low;
        int j = mid + 1;
        int k = low;

        while (i <= mid && j <= high) {
            if (helper[i].compareTo(helper[j]) <= 0) {
                array[k++] = helper[i++];
            } else {
                array[k++] = helper[j++];
            }
        }

        while (i <= mid) {
            array[k++] = helper[i++];
        }
    }
}
