package com.payroll.datastructure;

/**
 * ============================================================================
 * Assignment Data Structure 05: Manual Hash Table (CustomHashTable)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Manual Key-Value Hash Table implementation written from scratch using Separate Chaining
 * (Linked List bucket arrays) for collision resolution without java.util.HashMap.
 * 
 * Complexity Analysis:
 * -------------------
 * - Put (Insert): Average Time Complexity O(1), Worst Case O(N), Space Complexity O(N)
 * - Get (Search): Average Time Complexity O(1), Worst Case O(N), Space Complexity O(1)
 * - Remove (Delete): Average Time Complexity O(1), Worst Case O(N), Space Complexity O(1)
 * - Display / Traversal: Time Complexity O(Capacity + N), Space Complexity O(1)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Generics: Key {@code K} and Value {@code V} payload mapping.
 * - Encapsulation: Bucket array dynamic hashing and re-hashing are encapsulated.
 * 
 * Design Patterns Used:
 * --------------------
 * - Separate Chaining Hash Map Pattern.
 * 
 * @param <K> Key type
 * @param <V> Value type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomHashTable<K, V> {

    /**
     * Hash table key-value entry node.
     */
    private static class HashNode<K, V> {
        K key;
        V value;
        HashNode<K, V> next;

        HashNode(final K key, final V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode<K, V>[] buckets;
    private int capacity;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    @SuppressWarnings("unchecked")
    public CustomHashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new HashNode[capacity];
        this.size = 0;
    }

    /**
     * Computes bucket index for key using modulo hashing.
     */
    private int getBucketIndex(final K key) {
        if (key == null) return 0;
        final int hashCode = key.hashCode();
        return Math.abs(hashCode) % capacity;
    }

    /**
     * Inserts or updates key-value pair.
     * Average Time Complexity: O(1)
     *
     * @param key   Entry key
     * @param value Entry value
     */
    public void put(final K key, final V value) {
        final int index = getBucketIndex(key);
        HashNode<K, V> head = buckets[index];

        // Search for existing key to update value
        while (head != null) {
            if ((key == null && head.key == null) || (key != null && key.equals(head.key))) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        // Key not present; insert new node at chain head
        final HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        // Rehash if load factor threshold exceeded
        if ((1.0 * size) / capacity >= LOAD_FACTOR) {
            rehash();
        }
    }

    /**
     * Searches value associated with key.
     * Average Time Complexity: O(1)
     *
     * @param key Search key
     * @return Value if key exists, null if missing
     */
    public V get(final K key) {
        final int index = getBucketIndex(key);
        HashNode<K, V> head = buckets[index];
        while (head != null) {
            if ((key == null && head.key == null) || (key != null && key.equals(head.key))) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    /**
     * Removes key-value pair from hash table.
     * Average Time Complexity: O(1)
     *
     * @param key Key to remove
     * @return Deleted value, or null if key was absent
     */
    public V remove(final K key) {
        final int index = getBucketIndex(key);
        HashNode<K, V> head = buckets[index];
        HashNode<K, V> prev = null;

        while (head != null) {
            if ((key == null && head.key == null) || (key != null && key.equals(head.key))) {
                if (prev != null) {
                    prev.next = head.next;
                } else {
                    buckets[index] = head.next;
                }
                size--;
                return head.value;
            }
            prev = head;
            head = head.next;
        }
        return null;
    }

    /**
     * Checks if key exists in hash table.
     */
    public boolean containsKey(final K key) {
        return get(key) != null;
    }

    /**
     * Displays all non-empty bucket chains.
     */
    public void display() {
        System.out.println("CustomHashTable (Size: " + size + ", Capacity: " + capacity + "):");
        for (int i = 0; i < capacity; i++) {
            if (buckets[i] != null) {
                System.out.print(" Bucket " + i + ": ");
                HashNode<K, V> head = buckets[i];
                while (head != null) {
                    System.out.print("[" + head.key + " -> " + head.value + "] -> ");
                    head = head.next;
                }
                System.out.println("NULL");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        final HashNode<K, V>[] temp = buckets;
        capacity = capacity * 2;
        buckets = new HashNode[capacity];
        size = 0;

        for (final HashNode<K, V> headNode : temp) {
            HashNode<K, V> head = headNode;
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

    public int getSize() {
        return size;
    }
}
