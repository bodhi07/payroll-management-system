package com.payroll.member6.dsa;

import java.util.EmptyStackException;

/**
 * ============================================================================
 * Member 06 Data Structure: Custom Generic LIFO Stack
 * ============================================================================
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

    public void push(final T element) {
        final Node<T> newNode = new Node<>(element);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        final T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return size;
    }
}
