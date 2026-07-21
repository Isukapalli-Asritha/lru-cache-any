package com.lru;

public class DoublyLinkedList {

    public static class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Node head; // sentinel — least recently used side
    private final Node tail; // sentinel — most recently used side

    public DoublyLinkedList() {
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    /** Inserts node just before tail (marks it most recently used). */
    public void addLast(Node node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }

    /** Removes and returns the node just after head (least recently used). */
    public Node removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node lru = head.next;
        head.next = lru.next; // explicitly advance head to the new first node
        lru.next.prev = head;
        lru.prev = null;
        lru.next = null;
        return lru;
    }

    /** Unlinks a node from its current position and updates its neighbors. */
    public void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
    }

    public boolean isEmpty() {
        return head.next == tail;
    }
}
