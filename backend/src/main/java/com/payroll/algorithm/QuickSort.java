package com.payroll.algorithm;

/**
 * ============================================================================
 * Sorting Algorithm 05: Quick Sort (QuickSort)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Independent sorting class implementing In-Place Quick Sort using Lomuto partitioning.
 * 
 * Algorithm Explanation:
 * ---------------------
 * Chooses a pivot element, partitions array such that elements smaller than pivot
 * are on the left and larger elements are on the right, then recursively sorts partitions.
 * 
 * Complexity Analysis:
 * -------------------
 * - Best Case Time Complexity: O(N log N)
 * - Average Case Time Complexity: O(N log N) (Fastest general in-place sorting algorithm in practice)
 * - Worst Case Time Complexity: O(N^2) (Occurs when pivot is consistently smallest/largest element)
 * - Space Complexity: O(log N) (Recursive stack space)
 * 
 * Use Case:
 * ---------
 * High-performance internal memory sorting where in-place sorting is needed without extra O(N) allocation.
 * 
 * Comparison:
 * -----------
 * Faster in practice than Merge Sort due to smaller constant factors and cache locality,
 * but unstable and requires median-of-three pivot selection to prevent O(N^2) worst case.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class QuickSort {

    /**
     * Sorts generic comparable array using Quick Sort.
     *
     * @param <T> Comparable element type
     * @param array Target array to sort
     */
    public static <T extends Comparable<T>> void sort(final T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSortRecursive(array, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> void quickSortRecursive(final T[] array, final int low, final int high) {
        if (low < high) {
            // Partition index
            final int pi = partition(array, low, high);

            // Recursively sort elements before and after partition
            quickSortRecursive(array, low, pi - 1);
            quickSortRecursive(array, pi + 1, high);
        }
    }

    /**
     * Lomuto partition scheme taking last element as pivot.
     */
    private static <T extends Comparable<T>> int partition(final T[] array, final int low, final int high) {
        final T pivot = array[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                // Swap array[i] and array[j]
                final T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Swap array[i+1] and pivot (array[high])
        final T temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }
}
