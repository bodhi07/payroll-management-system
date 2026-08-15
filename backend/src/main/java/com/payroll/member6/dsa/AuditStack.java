package com.payroll.member6.dsa;

import com.payroll.member6.entity.AuditLog;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 06 DATA STRUCTURE: Custom Audit Log LIFO Stack (Last-In, First-Out)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Maintains a high-speed in-memory Last-In, First-Out (LIFO) stack
 *   of recent transactional and administrative security actions for instant
 *   audit replay, recent activity polling, and administrative rollback mechanisms.
 * - Structure: Dynamic Node-based Linked Stack.
 * - Time Complexity:
 *     * push() [Record Action]:   O(1) constant time.
 *     * pop() [Inspect Recent]:   O(1) constant time.
 *     * peek() [Top Audit Event]: O(1) constant time.
 * - Space Complexity: O(N) where N is number of recorded log items.
 */
public class AuditStack {

    private static class StackNode {
        AuditLog data;
        StackNode next;

        public StackNode(AuditLog data) {
            this.data = data;
            this.next = null;
        }
    }

    private StackNode top;
    private int size;

    public AuditStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Pushes a new audit log entry to the top of the stack in O(1) time.
     */
    public synchronized void push(AuditLog log) {
        if (log == null) return;
        StackNode newNode = new StackNode(log);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Pops and returns the most recent audit log entry in O(1) time.
     */
    public synchronized AuditLog pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        AuditLog value = top.data;
        top = top.next;
        size--;
        return value;
    }

    /**
     * Inspects the top most recent audit event without removing it in O(1) time.
     */
    public synchronized AuditLog peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return size;
    }

    /**
     * Converts stack entries to a chronological recent-first list in O(N) time.
     */
    public List<AuditLog> toRecentFirstList() {
        List<AuditLog> list = new ArrayList<>();
        StackNode current = top;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}
