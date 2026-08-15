package com.payroll.datastructure;

/**
 * ============================================================================
 * Assignment Data Structure 06: Manual Set ADT (CustomSet)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Manual generic implementation of a Set Abstract Data Type (ADT) guaranteeing unique
 * elements without java.util.HashSet. Uses composition over {@link CustomHashTable}.
 * 
 * Complexity Analysis:
 * -------------------
 * - Add (Insert): Average Time Complexity O(1), Space Complexity O(1)
 * - Contains (Search): Average Time Complexity O(1), Space Complexity O(1)
 * - Remove (Delete): Average Time Complexity O(1), Space Complexity O(1)
 * - Display / Traversal: Time Complexity O(Capacity + N), Space Complexity O(1)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Composition: Wraps an internal {@link CustomHashTable} instance.
 * 
 * Design Patterns Used:
 * --------------------
 * - Adapter / Wrapper Pattern.
 * 
 * @param <E> Element type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomSet<E> {

    private final CustomHashTable<E, Boolean> map;
    private static final Boolean PRESENT = Boolean.TRUE;

    public CustomSet() {
        this.map = new CustomHashTable<>();
    }

    /**
     * Adds element to set if not already present.
     * Time Complexity: Average O(1)
     *
     * @param element Element to insert
     * @return True if added, false if element already existed
     */
    public boolean add(final E element) {
        if (map.containsKey(element)) {
            return false;
        }
        map.put(element, PRESENT);
        return true;
    }

    /**
     * Removes element from set.
     * Time Complexity: Average O(1)
     *
     * @param element Element to remove
     * @return True if element was present and removed
     */
    public boolean remove(final E element) {
        return map.remove(element) != null;
    }

    /**
     * Checks if element exists in set.
     * Time Complexity: Average O(1)
     *
     * @param element Search target
     * @return True if element exists
     */
    public boolean contains(final E element) {
        return map.containsKey(element);
    }

    /**
     * Displays all set elements.
     */
    public void display() {
        System.out.print("CustomSet (Unique Elements Count: " + map.getSize() + "): ");
        map.display();
    }

    public int getSize() {
        return map.getSize();
    }
}
