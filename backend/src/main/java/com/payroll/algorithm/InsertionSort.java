package com.payroll.algorithm;

/**
 * ============================================================================
 * Sorting Algorithm 03: Insertion Sort (InsertionSort)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Independent sorting class implementing Insertion Sort.
 * 
 * Algorithm Explanation:
 * ---------------------
 * Builds the final sorted array one item at a time. Takes each element from the unsorted
 * portion and inserts it into its correct position in the sorted sub-array.
 * 
 * Complexity Analysis:
 * -------------------
 * - Best Case Time Complexity: O(N) (Array is already sorted)
 * - Average Case Time Complexity: O(N^2)
 * - Worst Case Time Complexity: O(N^2) (Reverse sorted array)
 * - Space Complexity: O(1) (In-place sorting)
 * 
 * Use Case:
 * ---------
 * Highly efficient for small datasets (N <= 20) and online stream data arriving incrementally.
 * 
 * Comparison:
 * -----------
 * Outperforms Bubble and Selection sort for small datasets and adaptive online streams.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class InsertionSort {

    /**
     * Sorts comparable array using Insertion Sort.
     *
     * @param <T> Comparable element type
     * @param array Target array to sort in-place
     */
    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        final int n = array.length;

        for (int i = 1; i < n; i++) {
            final T key = array[i];
            int j = i - 1;

            // Shift elements greater than key to one position ahead of current position
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
