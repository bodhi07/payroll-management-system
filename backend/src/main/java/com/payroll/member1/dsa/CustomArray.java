package com.payroll.member1.dsa;

/**
 * ============================================================================
 * Member 01 Data Structure: Custom Dynamic Array
 * ============================================================================
 */
public class CustomArray<T> {

    private Object[] data;
    private int size;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 10;

    public CustomArray() {
        this(DEFAULT_CAPACITY);
    }

    public CustomArray(final int initialCapacity) {
        this.capacity = Math.max(initialCapacity, 1);
        this.data = new Object[this.capacity];
        this.size = 0;
    }

    public void add(final T element) {
        if (size >= capacity) {
            resize(capacity * 2);
        }
        data[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public T get(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    public T remove(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        final T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
        return removed;
    }

    public int indexOf(final T element) {
        for (int i = 0; i < size; i++) {
            if ((element == null && data[i] == null) || (element != null && element.equals(data[i]))) {
                return i;
            }
        }
        return -1;
    }

    private void resize(final int newCapacity) {
        final Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
        capacity = newCapacity;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
