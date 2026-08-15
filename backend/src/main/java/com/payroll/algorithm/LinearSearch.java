package com.payroll.algorithm;

import java.util.List;

/**
 * ============================================================================
 * Searching Algorithm: Linear Search (LinearSearch)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Provides manual implementation of the Linear Search algorithm used for searching
 * employee profiles by name, email, NIC, or phone.
 * 
 * Algorithm Explanation:
 * ---------------------
 * Sequentially inspects each element in an array or collection from index 0 to N-1
 * until a matching target element is found or the end of the collection is reached.
 * 
 * Complexity Analysis:
 * -------------------
 * - Best Case Time Complexity: O(1) (Target is at first position)
 * - Average Case Time Complexity: O(N) (Target is in middle)
 * - Worst Case Time Complexity: O(N) (Target is at end or not present)
 * - Space Complexity: O(1) (In-place search requiring no extra allocation)
 * 
 * Use Case:
 * ---------
 * Ideal for small to medium unsorted lists or when dataset items are dynamically updated
 * without maintaining sorted order overhead. Used in Employee Management module.
 * 
 * Comparison:
 * -----------
 * Unlike Binary Search which requires sorted O(N log N) data and random access index O(1),
 * Linear Search works on unsorted datasets and linked structures in O(N) time.
 * 
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class LinearSearch {

    /**
     * Searches for a target string inside an array of elements.
     *
     * @param array Dataset array to search
     * @param target Target string to locate
     * @return 0-indexed position if found, -1 if target is absent
     */
    public static int search(final String[] array, final String target) {
        if (array == null || target == null) {
            return -1;
        }

        // Sequential traversal loop O(N)
        for (int i = 0; i < array.length; i++) {
            // Compare current element with target string
            if (target.equalsIgnoreCase(array[i])) {
                return i; // Element located at index i
            }
        }
        return -1; // Target not found
    }

    /**
     * Searches generic list for element.
     *
     * @param list  List collection
     * @param target Target element
     * @param <T>    Generic type
     * @return Index of element, or -1 if not found
     */
    public static <T> int search(final List<T> list, final T target) {
        if (list == null || target == null) {
            return -1;
        }

        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
