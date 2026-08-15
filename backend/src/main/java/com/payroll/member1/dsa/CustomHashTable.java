package com.payroll.member1.dsa;

/**
 * ============================================================================
 * Member 01 Data Structure: Custom Hash Table (Separate Chaining)
 * ============================================================================
 */
public class CustomHashTable<K, V> {

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

    private int getBucketIndex(final K key) {
        if (key == null) return 0;
        final int hashCode = key.hashCode();
        return Math.abs(hashCode) % capacity;
    }

    public void put(final K key, final V value) {
        final int index = getBucketIndex(key);
        HashNode<K, V> head = buckets[index];

        while (head != null) {
            if ((key == null && head.key == null) || (key != null && key.equals(head.key))) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        final HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
        size++;

        if ((1.0 * size) / capacity >= LOAD_FACTOR) {
            rehash();
        }
    }

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

    public boolean containsKey(final K key) {
        return get(key) != null;
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
