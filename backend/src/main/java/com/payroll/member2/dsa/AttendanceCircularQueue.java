package com.payroll.member2.dsa;

import com.payroll.member2.entity.Attendance;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 02 DATA STRUCTURE: Custom Attendance Circular Queue (FIFO Buffer)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Manages live daily shift punch-in events in First-In, First-Out (FIFO)
 *   order for sequential processing without memory fragmentation.
 * - Structure: Fixed/Resizable Circular Array Buffer with front, rear, and size pointers.
 * - Time Complexity:
 *     * enqueue() [Check-in event]: O(1) constant time.
 *     * dequeue() [Process event]:  O(1) constant time.
 *     * peek():                     O(1) constant time.
 * - Space Complexity: O(N) where N is maximum buffer capacity.
 */
public class AttendanceCircularQueue {

    private Attendance[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public AttendanceCircularQueue() {
        this(50);
    }

    public AttendanceCircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new Attendance[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Inserts a new attendance punch record at the rear in O(1) time.
     */
    public synchronized boolean enqueue(Attendance record) {
        if (isFull()) {
            expandCapacity();
        }
        rear = (rear + 1) % capacity;
        queue[rear] = record;
        size++;
        return true;
    }

    /**
     * Removes and returns the oldest attendance record at the front in O(1) time.
     */
    public synchronized Attendance dequeue() {
        if (isEmpty()) {
            return null;
        }
        Attendance item = queue[front];
        queue[front] = null;
        front = (front + 1) % capacity;
        size--;
        return item;
    }

    public synchronized Attendance peek() {
        if (isEmpty()) return null;
        return queue[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int getSize() {
        return size;
    }

    private void expandCapacity() {
        int newCapacity = capacity * 2;
        Attendance[] newQueue = new Attendance[newCapacity];

        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[(front + i) % capacity];
        }

        queue = newQueue;
        front = 0;
        rear = size - 1;
        capacity = newCapacity;
    }

    public List<Attendance> toList() {
        List<Attendance> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(queue[(front + i) % capacity]);
        }
        return list;
    }
}
