package com.payroll.algorithm;

/**
 * ============================================================================
 * Sorting Algorithm 01: Bubble Sort (BubbleSort)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Independent standalone sorting class implementing optimized Bubble Sort.
 * 
 * Algorithm Explanation:
 * ---------------------
 * Repeatedly steps through the array, compares adjacent elements, and swaps them
 * if they are in the wrong order. Larger elements "bubble up" to the end of the array.
 * Includes an early-exit `swapped` boolean optimization.
 * 
 * Complexity Analysis:
 * -------------------
 * - Best Case Time Complexity: O(N) (Array is already sorted; early exit on first pass)
 * - Average Case Time Complexity: O(N^2)
 * - Worst Case Time Complexity: O(N^2) (Array is sorted in reverse order)
 * - Space Complexity: O(1) (In-place sorting algorithm)
 * 
 * Use Case:
 * ---------
 * Educational purposes and nearly-sorted small arrays where simplicity is preferred.
 * 
 * Comparison:
 * -----------
 * Slower than O(N log N) algorithms (Merge Sort, Quick Sort), but requires O(1) auxiliary space.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class BubbleSort {

    /**
     * Sorts comparable generic array using optimized Bubble Sort.
     *
     * @param <T> Comparable element type
     * @param array Target array to sort in-place
     */
    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        final int n = array.length;
        boolean swapped;

        // Outer pass loop O(N)
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            // Inner comparison loop O(N)
            for (int j = 0; j < n - 1 - i; j++) {
                // Compare adjacent elements
                if (array[j].compareTo(array[j + 1]) > 0) {
                    // Swap elements
                    final T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            // Early exit optimization if no swaps occurred in pass
            if (!swapped) {
                break;
            }
        }
    }
}
