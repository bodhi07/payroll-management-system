package com.payroll.member2.dsa;

import java.util.NoSuchElementException;

/**
 * ============================================================================
 * Member 02 Data Structure: Custom Linked Queue (FIFO)
 * ============================================================================
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

    public void enqueue(final T element) {
        final Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        final T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        return front.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }
}
