package com.payroll.member3.dsa;

/**
 * ============================================================================
 * Member 03 Data Structure: Custom Binary Search Tree
 * ============================================================================
 */
public class CustomBinarySearchTree<T extends Comparable<T>> {

    public static class BSTNode<T> {
        public T data;
        public BSTNode<T> left;
        public BSTNode<T> right;

        public BSTNode(final T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private BSTNode<T> root;
    private int size;

    public CustomBinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    public void insert(final T value) {
        if (value == null) return;
        root = insertRecursive(root, value);
        size++;
    }

    private BSTNode<T> insertRecursive(final BSTNode<T> current, final T value) {
        if (current == null) {
            return new BSTNode<>(value);
        }
        if (value.compareTo(current.data) < 0) {
            current.left = insertRecursive(current.left, value);
        } else if (value.compareTo(current.data) > 0) {
            current.right = insertRecursive(current.right, value);
        }
        return current;
    }

    public boolean search(final T value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(final BSTNode<T> current, final T value) {
        if (current == null) return false;
        if (value.equals(current.data)) return true;
        return value.compareTo(current.data) < 0
                ? searchRecursive(current.left, value)
                : searchRecursive(current.right, value);
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
