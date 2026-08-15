package com.payroll.datastructure;

/**
 * ============================================================================
 * Assignment Data Structure 07: Manual Binary Search Tree (CustomBinarySearchTree)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Manual generic Binary Search Tree (BST) implementation without Java collection libraries.
 * Enforces Left < Root < Right ordering property.
 * 
 * Complexity Analysis:
 * -------------------
 * - Insert: Average Time O(log N), Worst Case O(N), Space O(1)
 * - Delete: Average Time O(log N), Worst Case O(N), Space O(1)
 * - Search: Average Time O(log N), Worst Case O(N), Space O(1)
 * - In-Order / Pre-Order / Post-Order Traversals: Time O(N), Space O(Height)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Polymorphic Bounded Type Parameters: {@code <T extends Comparable<T>>}.
 * - Recursion & Encapsulation.
 * 
 * Design Patterns Used:
 * --------------------
 * - Tree Structure Pattern.
 * - Visitor / Traversal Pattern.
 * 
 * @param <T> Comparable element type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomBinarySearchTree<T extends Comparable<T>> {

    /**
     * BST Node structure holding data payload, left child, right child.
     */
    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;

        Node(final T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node<T> root;

    public CustomBinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts value into BST maintaining ordering property.
     * Average Time Complexity: O(log N)
     *
     * @param data Payload to insert
     */
    public void insert(final T data) {
        root = insertRecursive(root, data);
    }

    private Node<T> insertRecursive(final Node<T> current, final T data) {
        if (current == null) {
            return new Node<>(data);
        }
        if (data.compareTo(current.data) < 0) {
            current.left = insertRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = insertRecursive(current.right, data);
        }
        return current;
    }

    /**
     * Searches for value in BST.
     * Average Time Complexity: O(log N)
     *
     * @param data Value to search
     * @return True if found
     */
    public boolean search(final T data) {
        return searchRecursive(root, data);
    }

    private boolean searchRecursive(final Node<T> current, final T data) {
        if (current == null) return false;
        if (data.compareTo(current.data) == 0) return true;
        return data.compareTo(current.data) < 0
                ? searchRecursive(current.left, data)
                : searchRecursive(current.right, data);
    }

    /**
     * Deletes node matching value from BST.
     * Average Time Complexity: O(log N)
     *
     * @param data Target value to remove
     */
    public void delete(final T data) {
        root = deleteRecursive(root, data);
    }

    private Node<T> deleteRecursive(final Node<T> current, final T data) {
        if (current == null) return null;

        if (data.compareTo(current.data) < 0) {
            current.left = deleteRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = deleteRecursive(current.right, data);
        } else {
            // Node found. Case 1: No children / 1 child
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;

            // Case 2: 2 children. Replace with In-Order Successor (smallest node in right subtree)
            current.data = findMin(current.right);
            current.right = deleteRecursive(current.right, current.data);
        }
        return current;
    }

    private T findMin(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    /**
     * Performs In-Order Traversal (Left -> Root -> Right) producing sorted sequence.
     */
    public void traverseInOrder() {
        System.out.print("In-Order Traversal: ");
        inOrderRecursive(root);
        System.out.println();
    }

    private void inOrderRecursive(final Node<T> node) {
        if (node != null) {
            inOrderRecursive(node.left);
            System.out.print(node.data + " ");
            inOrderRecursive(node.right);
        }
    }

    /**
     * Performs Pre-Order Traversal (Root -> Left -> Right).
     */
    public void traversePreOrder() {
        System.out.print("Pre-Order Traversal: ");
        preOrderRecursive(root);
        System.out.println();
    }

    private void preOrderRecursive(final Node<T> node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrderRecursive(node.left);
            preOrderRecursive(node.right);
        }
    }

    /**
     * Performs Post-Order Traversal (Left -> Right -> Root).
     */
    public void traversePostOrder() {
        System.out.print("Post-Order Traversal: ");
        postOrderRecursive(root);
        System.out.println();
    }

    private void postOrderRecursive(final Node<T> node) {
        if (node != null) {
            postOrderRecursive(node.left);
            postOrderRecursive(node.right);
            System.out.print(node.data + " ");
        }
    }

    /**
     * Displays BST in-order structure.
     */
    public void display() {
        traverseInOrder();
    }
}
