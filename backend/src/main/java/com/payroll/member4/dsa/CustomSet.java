package com.payroll.member4.dsa;

/**
 * ============================================================================
 * Member 04 Data Structure: Custom Set (Unique Elements)
 * ============================================================================
 */
public class CustomSet<T> {

    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public CustomSet() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public boolean add(final T element) {
        if (contains(element)) return false;
        if (size >= elements.length) {
            final Object[] newElements = new Object[elements.length * 2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
        elements[size++] = element;
        return true;
    }

    public boolean contains(final T element) {
        for (int i = 0; i < size; i++) {
            if ((element == null && elements[i] == null) || (element != null && element.equals(elements[i]))) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(final T element) {
        for (int i = 0; i < size; i++) {
            if ((element == null && elements[i] == null) || (element != null && element.equals(elements[i]))) {
                elements[i] = elements[size - 1];
                elements[size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
