package com.payroll.datastructure;

import java.util.NoSuchElementException;

/**
 * ============================================================================
 * Assignment Data Structure 04: Manual FIFO Queue (CustomQueue)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Manual generic implementation of a First-In-First-Out (FIFO) Queue ADT using
 * linked node references without built-in Queue or ArrayDeque classes.
 * 
 * Complexity Analysis:
 * -------------------
 * - Enqueue (Insert): Time Complexity O(1), Space Complexity O(1)
 * - Dequeue (Delete): Time Complexity O(1), Space Complexity O(1)
 * - Peek (Front): Time Complexity O(1), Space Complexity O(1)
 * - Search: Time Complexity O(N), Space Complexity O(1)
 * - Display / Traversal: Time Complexity O(N), Space Complexity O(1)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Conceals front/rear node pointers.
 * 
 * Design Patterns Used:
 * --------------------
 * - Queue ADT Pattern.
 * 
 * @param <T> Generic payload type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomQueue<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(final T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Inserts element at rear of queue (Enqueue).
     * Time Complexity: O(1)
     *
     * @param element Payload to insert
     */
    public void enqueue(final T element) {
        final Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /**
     * Removes and returns front element of queue (Dequeue).
     * Time Complexity: O(1)
     *
     * @return Element payload from front
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        final T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /**
     * Inspects front element without removing it.
     * Time Complexity: O(1)
     *
     * @return Front element payload
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return front.data;
    }

    /**
     * Searches for element position in queue from front to rear.
     * Time Complexity: O(N)
     *
     * @param element Target element
     * @return 0-indexed position if found, -1 if not found
     */
    public int search(final T element) {
        int index = 0;
        Node<T> current = front;
        while (current != null) {
            if ((element == null && current.data == null) || (element != null && element.equals(current.data))) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Displays queue elements from front to rear.
     * Time Complexity: O(N)
     */
    public void display() {
        System.out.print("CustomQueue (FRONT -> REAR): ");
        Node<T> current = front;
        while (current != null) {
            System.out.print("[" + current.data + "] ");
            current = current.next;
        }
        System.out.println("(Size: " + size + ")");
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int getSize() {
        return size;
    }
}
