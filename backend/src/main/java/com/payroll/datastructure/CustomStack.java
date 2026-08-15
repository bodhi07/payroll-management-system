package com.payroll.datastructure;

import java.util.EmptyStackException;

/**
 * ============================================================================
 * Assignment Data Structure 03: Manual LIFO Stack (CustomStack)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Manual generic implementation of a Last-In-First-Out (LIFO) Stack ADT without
 * using java.util.Stack or Deque. Uses node link pointer references.
 * 
 * Complexity Analysis:
 * -------------------
 * - Push: Time Complexity O(1), Space Complexity O(1)
 * - Pop: Time Complexity O(1), Space Complexity O(1)
 * - Peek (Top): Time Complexity O(1), Space Complexity O(1)
 * - Search: Time Complexity O(N), Space Complexity O(1)
 * - Traversal / Display: Time Complexity O(N), Space Complexity O(1)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Abstraction: Encapsulates top node reference and LIFO operations.
 * 
 * Design Patterns Used:
 * --------------------
 * - Stack ADT Pattern.
 * 
 * @param <T> Generic payload type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomStack<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(final T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> top;
    private int size;

    public CustomStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Pushes element onto top of stack.
     * Time Complexity: O(1)
     *
     * @param element Element payload to push
     */
    public void push(final T element) {
        final Node<T> newNode = new Node<>(element);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Removes and returns top element from stack (Pop).
     * Time Complexity: O(1)
     *
     * @return Element payload from top of stack
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        final T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /**
     * Inspects top element without removing it.
     * Time Complexity: O(1)
     *
     * @return Top element payload
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return top.data;
    }

    /**
     * Searches for element position relative to top of stack.
     * Time Complexity: O(N)
     *
     * @param element Search element
     * @return 1-based distance from top if found, -1 if not present
     */
    public int search(final T element) {
        int distance = 1;
        Node<T> current = top;
        while (current != null) {
            if ((element == null && current.data == null) || (element != null && element.equals(current.data))) {
                return distance;
            }
            current = current.next;
            distance++;
        }
        return -1;
    }

    /**
     * Displays stack contents from top to bottom.
     * Time Complexity: O(N)
     */
    public void display() {
        System.out.print("CustomStack (TOP -> BOTTOM): ");
        Node<T> current = top;
        while (current != null) {
            System.out.print("[" + current.data + "] ");
            current = current.next;
        }
        System.out.println("(Size: " + size + ")");
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return size;
    }
}
