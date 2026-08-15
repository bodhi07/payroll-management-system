package com.payroll.datastructure;

/**
 * ============================================================================
 * Assignment Data Structure 08: Manual Self-Balancing AVL Tree (CustomAVLTree)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Manual generic AVL Tree implementation written from scratch. Automatically maintains
 * height balance factor (Left Height - Right Height within -1, 0, 1) using Single and Double Rotations.
 * 
 * Complexity Analysis:
 * -------------------
 * - Insert: Time Complexity STRICT O(log N), Space Complexity O(1)
 * - Delete: Time Complexity STRICT O(log N), Space Complexity O(1)
 * - Search: Time Complexity STRICT O(log N), Space Complexity O(1)
 * - Traversal: Time Complexity O(N), Space Complexity O(log N)
 * 
 * Rotations Implemented:
 * ---------------------
 * 1. Right Rotation (LL Heavy)
 * 2. Left Rotation (RR Heavy)
 * 3. Left-Right Rotation (LR Heavy)
 * 4. Right-Left Rotation (RL Heavy)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Polymorphism & Generics: {@code <T extends Comparable<T>>}.
 * - Encapsulation.
 * 
 * @param <T> Comparable element type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomAVLTree<T extends Comparable<T>> {

    private static class Node<T> {
        T data;
        int height;
        Node<T> left;
        Node<T> right;

        Node(final T data) {
            this.data = data;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    private Node<T> root;

    public CustomAVLTree() {
        this.root = null;
    }

    private int height(final Node<T> node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(final Node<T> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    /**
     * Right Rotation (LL Case).
     */
    private Node<T> rightRotate(final Node<T> y) {
        final Node<T> x = y.left;
        final Node<T> T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    /**
     * Left Rotation (RR Case).
     */
    private Node<T> leftRotate(final Node<T> x) {
        final Node<T> y = x.right;
        final Node<T> T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    /**
     * Inserts value into AVL tree and performs balancing rotations.
     * Time Complexity: Strict O(log N)
     *
     * @param data Element to insert
     */
    public void insert(final T data) {
        root = insertRecursive(root, data);
    }

    private Node<T> insertRecursive(Node<T> node, final T data) {
        if (node == null) {
            return new Node<>(data);
        }

        if (data.compareTo(node.data) < 0) {
            node.left = insertRecursive(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insertRecursive(node.right, data);
        } else {
            return node; // Duplicate keys not allowed
        }

        // Update height
        node.height = 1 + Math.max(height(node.left), height(node.right));
        final int balance = getBalance(node);

        // LL Case -> Right Rotate
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rightRotate(node);
        }

        // RR Case -> Left Rotate
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return leftRotate(node);
        }

        // LR Case -> Left-Right Rotate
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL Case -> Right-Left Rotate
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    /**
     * Searches for element in AVL Tree.
     * Time Complexity: O(log N)
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
     * Deletes value from AVL tree and rebalances.
     * Time Complexity: Strict O(log N)
     */
    public void delete(final T data) {
        root = deleteRecursive(root, data);
    }

    private Node<T> deleteRecursive(Node<T> root, final T data) {
        if (root == null) return root;

        if (data.compareTo(root.data) < 0) {
            root.left = deleteRecursive(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = deleteRecursive(root.right, data);
        } else {
            if ((root.left == null) || (root.right == null)) {
                final Node<T> temp = root.left != null ? root.left : root.right;
                if (temp == null) {
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                final Node<T> temp = getMinValueNode(root.right);
                root.data = temp.data;
                root.right = deleteRecursive(root.right, temp.data);
            }
        }

        if (root == null) return root;

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        final int balance = getBalance(root);

        // LL Balance
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }
        // LR Balance
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        // RR Balance
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }
        // RL Balance
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private Node<T> getMinValueNode(Node<T> node) {
        Node<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Traverses AVL Tree in-order.
     */
    public void display() {
        System.out.print("AVL Tree In-Order: ");
        inOrder(root);
        System.out.println();
    }

    private void inOrder(final Node<T> node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.data + "(h=" + node.height + ") ");
            inOrder(node.right);
        }
    }
}
