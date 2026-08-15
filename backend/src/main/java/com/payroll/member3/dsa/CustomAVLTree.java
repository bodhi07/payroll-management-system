package com.payroll.member3.dsa;

/**
 * ============================================================================
 * Member 03 Data Structure: Custom Balanced AVL Tree
 * ============================================================================
 */
public class CustomAVLTree<T extends Comparable<T>> {

    public static class AVLNode<T> {
        public T data;
        public AVLNode<T> left;
        public AVLNode<T> right;
        public int height;

        public AVLNode(final T data) {
            this.data = data;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    private AVLNode<T> root;
    private int size;

    public CustomAVLTree() {
        this.root = null;
        this.size = 0;
    }

    private int height(final AVLNode<T> node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(final AVLNode<T> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode<T> rightRotate(final AVLNode<T> y) {
        final AVLNode<T> x = y.left;
        final AVLNode<T> t2 = x.right;
        x.right = y;
        y.left = t2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private AVLNode<T> leftRotate(final AVLNode<T> x) {
        final AVLNode<T> y = x.right;
        final AVLNode<T> t2 = y.left;
        y.left = x;
        x.right = t2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    public void insert(final T value) {
        if (value == null) return;
        root = insertRec(root, value);
        size++;
    }

    private AVLNode<T> insertRec(AVLNode<T> node, final T value) {
        if (node == null) return new AVLNode<>(value);

        if (value.compareTo(node.data) < 0) {
            node.left = insertRec(node.left, value);
        } else if (value.compareTo(node.data) > 0) {
            node.right = insertRec(node.right, value);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        final int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && value.compareTo(node.left.data) < 0) {
            return rightRotate(node);
        }
        // Right Right Case
        if (balance < -1 && value.compareTo(node.right.data) > 0) {
            return leftRotate(node);
        }
        // Left Right Case
        if (balance > 1 && value.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // Right Left Case
        if (balance < -1 && value.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public boolean search(final T value) {
        AVLNode<T> curr = root;
        while (curr != null) {
            if (value.equals(curr.data)) return true;
            curr = value.compareTo(curr.data) < 0 ? curr.left : curr.right;
        }
        return false;
    }

    public int getSize() {
        return size;
    }
}
