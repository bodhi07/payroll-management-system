package com.payroll.algorithm;

/**
 * ============================================================================
 * Sorting Algorithm 02: Selection Sort (SelectionSort)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Independent sorting class implementing Selection Sort.
 * 
 * Algorithm Explanation:
 * ---------------------
 * Divides the input array into a sorted sublist and an unsorted sublist.
 * Repeatedly selects the minimum element from the unsorted sublist and swaps it
 * into the leftmost unsorted index.
 * 
 * Complexity Analysis:
 * -------------------
 * - Best Case Time Complexity: O(N^2) (Always scans entire unsorted portion)
 * - Average Case Time Complexity: O(N^2)
 * - Worst Case Time Complexity: O(N^2)
 * - Space Complexity: O(1) (In-place sorting)
 * 
 * Use Case:
 * ---------
 * Useful when memory write operations are expensive because it makes at most O(N) swaps.
 * 
 * Comparison:
 * -----------
 * Performs fewer swaps than Bubble Sort, but has a fixed O(N^2) comparison overhead.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class SelectionSort {

    /**
     * Sorts comparable array using Selection Sort.
     *
     * @param <T> Comparable element type
     * @param array Target array to sort in-place
     */
    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        final int n = array.length;

        // Loop over unsorted boundary O(N)
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            // Find minimum element in unsorted subarray O(N)
            for (int j = i + 1; j < n; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            // Swap minimum element with current index
            if (minIndex != i) {
                final T temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
    }
}
