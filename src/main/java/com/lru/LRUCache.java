package com.lru;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    private final int capacity;
    private final Map<Integer, DoublyLinkedList.Node> map;
    private final DoublyLinkedList list;

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.list = new DoublyLinkedList();
    }

    /**
     * Returns the value for the given key, or -1 if not present.
     * Moves the accessed entry to most recently used position.
     */
    public int get(Integer key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        DoublyLinkedList.Node node = map.get(key);
        if (node == null) {
            return -1;
        }
        moveToTail(node);
        return node.value;
    }

    /**
     * Inserts or updates the entry for the given key.
     * On insert, evicts the least recently used entry if at capacity.
     */
    public void put(Integer key, Integer value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }

        DoublyLinkedList.Node existing = map.get(key);
        if (existing != null) {
            existing.value = value;
            moveToTail(existing);
            return;
        }

        if (map.size() == capacity) {
            DoublyLinkedList.Node evicted = list.removeFirst();
            map.remove(evicted.key);
        }

        DoublyLinkedList.Node node = new DoublyLinkedList.Node(key, value);
        list.addLast(node);
        map.put(key, node);
    }

    public int size() {
        return map.size();
    }

    private void moveToTail(DoublyLinkedList.Node node) {
        list.remove(node);
        list.addLast(node);
    }
}
