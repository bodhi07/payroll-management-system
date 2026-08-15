package com.payroll.datastructure;

/**
 * ============================================================================
 * Assignment Data Structure 01: Manual Dynamic Array (CustomArray)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Custom generic dynamic array implementation written from scratch without standard
 * Java collection wrappers (e.g., ArrayList). Supports dynamic resizing, insertion,
 * deletion, linear search, element display, and traversal.
 * 
 * Complexity Analysis:
 * -------------------
 * - Insert at end (Amortized): Time Complexity O(1), Space Complexity O(1)
 * - Insert at index: Time Complexity O(N), Space Complexity O(1)
 * - Delete by index/value: Time Complexity O(N), Space Complexity O(1)
 * - Search (Linear): Time Complexity O(N), Space Complexity O(1)
 * - Access by index: Time Complexity O(1), Space Complexity O(1)
 * - Display / Traversal: Time Complexity O(N), Space Complexity O(1)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Generics (Type Abstraction): {@code CustomArray<T>} supports storing any object type.
 * - Encapsulation: Internal object array, current size, and capacity are hidden behind private fields.
 * 
 * Design Patterns Used:
 * --------------------
 * - Custom Data Structure Pattern.
 * - Iterator Pattern (internal traversal method).
 * 
 * @param <T> Generic element type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomArray<T> {

    private Object[] data;
    private int size;
    private int capacity;

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Default constructor initializing array with capacity 10.
     */
    public CustomArray() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor initializing array with custom initial capacity.
     *
     * @param initialCapacity Starting internal array capacity
     */
    public CustomArray(final int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        this.capacity = initialCapacity;
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    /**
     * Inserts element at the end of the dynamic array. Doubles capacity if full.
     * Time Complexity: Amortized O(1)
     *
     * @param element Element to insert
     */
    public void insert(final T element) {
        if (size == capacity) {
            resize(capacity * 2);
        }
        data[size++] = element;
    }

    /**
     * Inserts element at specified index shifting elements right.
     * Time Complexity: O(N)
     *
     * @param index Target index
     * @param element Element to insert
     */
    public void insertAt(final int index, final T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (size == capacity) {
            resize(capacity * 2);
        }
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }

    /**
     * Deletes element at specified index shifting remaining elements left.
     * Time Complexity: O(N)
     *
     * @param index Target index to delete
     * @return Removed element
     */
    @SuppressWarnings("unchecked")
    public T deleteAt(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        final T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null; // Clear reference for GC
        size--;
        return removed;
    }

    /**
     * Deletes first occurrence of element matching value.
     * Time Complexity: O(N)
     *
     * @param element Value to delete
     * @return True if element was found and deleted
     */
    public boolean delete(final T element) {
        final int index = search(element);
        if (index != -1) {
            deleteAt(index);
            return true;
        }
        return false;
    }

    /**
     * Searches for element using Linear Search algorithm.
     * Time Complexity: O(N)
     *
     * @param element Search target
     * @return Zero-indexed location if found, -1 if not found
     */
    public int search(final T element) {
        for (int i = 0; i < size; i++) {
            if ((element == null && data[i] == null) || (element != null && element.equals(data[i]))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retrieves element at index.
     * Time Complexity: O(1)
     *
     * @param index Element position
     * @return Element at index
     */
    @SuppressWarnings("unchecked")
    public T get(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return (T) data[index];
    }

    /**
     * Returns total number of inserted elements.
     *
     * @return Size of dynamic array
     */
    public int getSize() {
        return size;
    }

    /**
     * Traverses and displays array content to standard console.
     * Time Complexity: O(N)
     */
    public void display() {
        System.out.print("CustomArray [ ");
        for (int i = 0; i < size; i++) {
            System.out.print(data[i] + (i < size - 1 ? ", " : ""));
        }
        System.out.println(" ] (Size: " + size + ", Capacity: " + capacity + ")");
    }

    /**
     * Resizes internal buffer array.
     *
     * @param newCapacity Target buffer capacity
     */
    private void resize(final int newCapacity) {
        final Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        this.data = newData;
        this.capacity = newCapacity;
    }
}
