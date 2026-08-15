package com.payroll.algorithm;

/**
 * ============================================================================
 * Sorting Algorithm 04: Merge Sort (MergeSort)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Independent sorting class implementing Divide and Conquer Merge Sort.
 * 
 * Algorithm Explanation:
 * ---------------------
 * Divides array into two halves recursively until sub-arrays have 1 element,
 * then merges sorted sub-arrays into a unified sorted array. Guaranteed stable sort.
 * 
 * Complexity Analysis:
 * -------------------
 * - Best Case Time Complexity: O(N log N)
 * - Average Case Time Complexity: O(N log N)
 * - Worst Case Time Complexity: O(N log N) (Guaranteed O(N log N) worst-case performance)
 * - Space Complexity: O(N) (Requires auxiliary temporary array storage)
 * 
 * Use Case:
 * ---------
 * Ideal when predictable O(N log N) worst-case timing and stability are mandatory.
 * 
 * Comparison:
 * -----------
 * Unlike Quick Sort which has O(N^2) worst case, Merge Sort guarantees O(N log N)
 * time complexity at the cost of O(N) auxiliary memory allocation.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class MergeSort {

    /**
     * Sorts generic comparable array using Divide and Conquer Merge Sort.
     *
     * @param <T> Comparable element type
     * @param array Target array to sort
     */
    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        mergeSortRecursive(array, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> void mergeSortRecursive(final T[] array, final int left, final int right) {
        if (left < right) {
            final int mid = left + (right - left) / 2;

            // Divide step: Sort left half
            mergeSortRecursive(array, left, mid);

            // Divide step: Sort right half
            mergeSortRecursive(array, mid + 1, right);

            // Conquer step: Merge sorted halves
            merge(array, left, mid, right);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void merge(final T[] array, final int left, final int mid, final int right) {
        final int n1 = mid - left + 1;
        final int n2 = right - mid;

        final Object[] leftArr = new Object[n1];
        final Object[] rightArr = new Object[n2];

        for (int i = 0; i < n1; i++) leftArr[i] = array[left + i];
        for (int j = 0; j < n2; j++) rightArr[j] = array[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            final T leftVal = (T) leftArr[i];
            final T rightVal = (T) rightArr[j];

            if (leftVal.compareTo(rightVal) <= 0) {
                array[k] = leftVal;
                i++;
            } else {
                array[k] = rightVal;
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = (T) leftArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = (T) rightArr[j];
            j++;
            k++;
        }
    }
}
