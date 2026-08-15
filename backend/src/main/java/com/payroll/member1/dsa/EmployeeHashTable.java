package com.payroll.member1.dsa;

import com.payroll.member1.entity.Employee;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 01 DATA STRUCTURE: Custom Employee Hash Table (Separate Chaining)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Provides O(1) average-time complexity lookup, insertion, and deletion
 *   of Employee records using Employee NIC or Employee ID as the unique key.
 * - Collision Resolution Technique: Separate Chaining with LinkedList buckets.
 * - Time Complexity:
 *     * Insertion: O(1) average, O(n) worst-case (if all keys hash to one bucket).
 *     * Search:    O(1) average.
 *     * Deletion:  O(1) average.
 * - Space Complexity: O(n + m) where n = total employees, m = bucket capacity.
 */
public class EmployeeHashTable {

    private static class HashNode {
        String key;
        Employee value;
        HashNode next;

        public HashNode(String key, Employee value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode[] buckets;
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    public EmployeeHashTable() {
        this(16);
    }

    public EmployeeHashTable(int capacity) {
        this.capacity = capacity;
        this.buckets = new HashNode[capacity];
        this.size = 0;
    }

    /**
     * Custom polynomial hash function generating index for String keys (NIC / Emp Number)
     */
    private int getBucketIndex(String key) {
        if (key == null) return 0;
        int hashCode = 0;
        for (int i = 0; i < key.length(); i++) {
            hashCode = (31 * hashCode + key.charAt(i)) % capacity;
        }
        return Math.abs(hashCode);
    }

    /**
     * Inserts or updates an employee in O(1) average time.
     */
    public void put(String key, Employee employee) {
        int bucketIndex = getBucketIndex(key);
        HashNode head = buckets[bucketIndex];

        // Check if key already exists in bucket chain -> update
        while (head != null) {
            if (head.key.equalsIgnoreCase(key)) {
                head.value = employee;
                return;
            }
            head = head.next;
        }

        // Insert new node at head of bucket chain (O(1))
        size++;
        head = buckets[bucketIndex];
        HashNode newNode = new HashNode(key, employee);
        newNode.next = head;
        buckets[bucketIndex] = newNode;

        // Dynamic resizing if load factor exceeded
        if ((1.0 * size) / capacity >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }
    }

    /**
     * Retrieves an employee in O(1) average time.
     */
    public Employee get(String key) {
        int bucketIndex = getBucketIndex(key);
        HashNode head = buckets[bucketIndex];

        while (head != null) {
            if (head.key.equalsIgnoreCase(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * Removes an employee from the hash table in O(1) average time.
     */
    public Employee remove(String key) {
        int bucketIndex = getBucketIndex(key);
        HashNode head = buckets[bucketIndex];
        HashNode prev = null;

        while (head != null) {
            if (head.key.equalsIgnoreCase(key)) {
                break;
            }
            prev = head;
            head = head.next;
        }

        if (head == null) return null;

        size--;
        if (prev != null) {
            prev.next = head.next;
        } else {
            buckets[bucketIndex] = head.next;
        }

        return head.value;
    }

    private void resize() {
        HashNode[] oldBuckets = buckets;
        capacity = capacity * 2;
        buckets = new HashNode[capacity];
        size = 0;

        for (HashNode headNode : oldBuckets) {
            while (headNode != null) {
                put(headNode.key, headNode.value);
                headNode = headNode.next;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public List<Employee> getAllValues() {
        List<Employee> list = new ArrayList<>();
        for (HashNode head : buckets) {
            while (head != null) {
                list.add(head.value);
                head = head.next;
            }
        }
        return list;
    }
}
