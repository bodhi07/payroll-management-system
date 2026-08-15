package com.payroll.datastructure;

/**
 * ============================================================================
 * Assignment Data Structure 02: Singly Linked List (CustomLinkedList)
 * ============================================================================
 * 
 * Why This Class Exists:
 * --------------------
 * Manual generic implementation of a Singly Linked List without Java built-in LinkedList.
 * Manages head and tail node pointers for dynamic memory allocation.
 * 
 * Complexity Analysis:
 * -------------------
 * - Insert at head (insertFirst): Time Complexity O(1), Space Complexity O(1)
 * - Insert at tail (insertLast): Time Complexity O(1), Space Complexity O(1)
 * - Delete head (deleteFirst): Time Complexity O(1), Space Complexity O(1)
 * - Delete tail / specific value: Time Complexity O(N), Space Complexity O(1)
 * - Search: Time Complexity O(N), Space Complexity O(1)
 * - Traversal / Display: Time Complexity O(N), Space Complexity O(1)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Encapsulation: Inner static Node class conceals pointer pointers.
 * - Composition / Association: Nodes link to successor node references.
 * 
 * Design Patterns Used:
 * --------------------
 * - Node Pointer List Structure Pattern.
 * 
 * @param <T> Generic element payload type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomLinkedList<T> {

    /**
     * Inner static Node representation holding data payload and next reference.
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(final T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Inserts new element at beginning of list (Head).
     * Time Complexity: O(1)
     *
     * @param data Payload element
     */
    public void insertFirst(final T data) {
        final Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    /**
     * Inserts new element at end of list (Tail).
     * Time Complexity: O(1)
     *
     * @param data Payload element
     */
    public void insertLast(final T data) {
        final Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Alias for insertLast.
     *
     * @param data Payload element
     */
    public void insert(final T data) {
        insertLast(data);
    }

    /**
     * Deletes head element.
     * Time Complexity: O(1)
     *
     * @return Data payload of deleted node
     */
    public T deleteFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot delete from an empty linked list.");
        }
        final T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return data;
    }

    /**
     * Deletes first node matching specified value.
     * Time Complexity: O(N)
     *
     * @param target Target data element to remove
     * @return True if found and removed
     */
    public boolean delete(final T target) {
        if (isEmpty()) return false;

        if (head.data.equals(target)) {
            deleteFirst();
            return true;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(target)) {
            current = current.next;
        }

        if (current.next != null) {
            if (current.next == tail) {
                tail = current;
            }
            current.next = current.next.next;
            size--;
            return true;
        }
        return false;
    }

    /**
     * Searches list for element value.
     * Time Complexity: O(N)
     *
     * @param target Element to search
     * @return True if present in linked list
     */
    public boolean search(final T target) {
        Node<T> current = head;
        while (current != null) {
            if ((target == null && current.data == null) || (target != null && target.equals(current.data))) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Traverses and prints linked list nodes.
     * Time Complexity: O(N)
     */
    public void display() {
        System.out.print("CustomLinkedList: ");
        Node<T> current = head;
        while (current != null) {
            System.out.print("[" + current.data + "] -> ");
            current = current.next;
        }
        System.out.println("NULL (Size: " + size + ")");
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }
}
