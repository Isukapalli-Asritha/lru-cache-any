package com.lru;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    // -------------------------------------------------------------------------
    // Capacity = 1 (minimum valid user input)
    // -------------------------------------------------------------------------

    @Test
    void capacityOne_evictsOnEveryInsert() {
        LRUCache cache = new LRUCache(1);
        cache.put(1, 10);
        cache.put(2, 20); // evicts key 1

        assertEquals(-1, cache.get(1), "Key 1 should have been evicted");
        assertEquals(20, cache.get(2));
        assertEquals(1, cache.size());
    }

    @Test
    void capacityOne_updateRetainsOnlyThatEntry() {
        LRUCache cache = new LRUCache(1);
        cache.put(1, 10);
        cache.put(1, 99); // update, not eviction

        assertEquals(99, cache.get(1));
        assertEquals(1, cache.size());
    }

    // -------------------------------------------------------------------------
    // Capacity < 5
    // -------------------------------------------------------------------------

    @Test
    void capacityLessThan5_evictsLRUWhenFull() {
        LRUCache cache = new LRUCache(3);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);

        // inserting 4th evicts key 1 (LRU)
        cache.put(4, 40);

        assertEquals(-1, cache.get(1), "Key 1 should have been evicted");
        assertEquals(20, cache.get(2));
        assertEquals(30, cache.get(3));
        assertEquals(40, cache.get(4));
    }

    @Test
    void capacityLessThan5_getPromotesToMRU() {
        LRUCache cache = new LRUCache(3);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);

        // access key 1 — it becomes MRU, so key 2 becomes LRU
        cache.get(1);
        cache.put(4, 40); // evicts key 2

        assertEquals(-1, cache.get(2), "Key 2 should have been evicted");
        assertEquals(10, cache.get(1));
        assertEquals(30, cache.get(3));
        assertEquals(40, cache.get(4));
    }

    @Test
    void capacityLessThan5_updateKeepsCorrectEvictionOrder() {
        LRUCache cache = new LRUCache(3);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);

        // update key 1 — it becomes MRU, so key 2 is now LRU
        cache.put(1, 100);
        cache.put(4, 40); // evicts key 2

        assertEquals(-1, cache.get(2), "Key 2 should have been evicted");
        assertEquals(100, cache.get(1));
        assertEquals(30, cache.get(3));
        assertEquals(40, cache.get(4));
    }

    // -------------------------------------------------------------------------
    // Capacity = 5
    // -------------------------------------------------------------------------

    @Test
    void capacityEqualTo5_fillsWithoutEviction() {
        LRUCache cache = new LRUCache(5);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        cache.put(4, 40);
        cache.put(5, 50);

        assertEquals(5, cache.size());
        assertEquals(10, cache.get(1));
        assertEquals(20, cache.get(2));
        assertEquals(30, cache.get(3));
        assertEquals(40, cache.get(4));
        assertEquals(50, cache.get(5));
    }

    @Test
    void capacityEqualTo5_evictsLRUOnSixthInsert() {
        LRUCache cache = new LRUCache(5);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        cache.put(4, 40);
        cache.put(5, 50);

        // key 1 is LRU — should be evicted
        cache.put(6, 60);

        assertEquals(-1, cache.get(1), "Key 1 should have been evicted");
        assertEquals(5, cache.size());
        assertEquals(60, cache.get(6));
    }

    @Test
    void capacityEqualTo5_updateDoesNotGrowBeyondCapacity() {
        LRUCache cache = new LRUCache(5);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        cache.put(4, 40);
        cache.put(5, 50);

        cache.put(3, 300); // update, not a new entry

        assertEquals(5, cache.size());
        assertEquals(300, cache.get(3));
    }

    @Test
    void capacityEqualTo5_updateMakesKeyMRUSoOtherKeyEvicted() {
        LRUCache cache = new LRUCache(5);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        cache.put(4, 40);
        cache.put(5, 50);

        // update key 1 — it becomes MRU, so key 2 is now LRU
        cache.put(1, 100);
        cache.put(6, 60); // evicts key 2

        assertEquals(-1, cache.get(2), "Key 2 should have been evicted");
        assertEquals(100, cache.get(1));
        assertEquals(60, cache.get(6));
    }

    // -------------------------------------------------------------------------
    // Capacity > 5
    // -------------------------------------------------------------------------

    @Test
    void capacityGreaterThan5_evictsLRUCorrectly() {
        LRUCache cache = new LRUCache(7);
        for (int i = 1; i <= 7; i++) {
            cache.put(i, i * 10);
        }

        // access keys 1 and 2 so they become MRU; 3 becomes LRU
        cache.get(1);
        cache.get(2);

        // inserting 3 more evicts keys 3, 4, 5 in order
        cache.put(8, 80);
        cache.put(9, 90);
        cache.put(10, 100);

        assertEquals(-1, cache.get(3), "Key 3 should have been evicted");
        assertEquals(-1, cache.get(4), "Key 4 should have been evicted");
        assertEquals(-1, cache.get(5), "Key 5 should have been evicted");
        assertEquals(60, cache.get(6));
        assertEquals(70, cache.get(7));
        assertEquals(10, cache.get(1));
        assertEquals(20, cache.get(2));
    }

    @Test
    void capacityGreaterThan5_sizeNeverExceedsCapacity() {
        LRUCache cache = new LRUCache(7);
        for (int i = 1; i <= 20; i++) {
            cache.put(i, i * 10);
            assertTrue(cache.size() <= 7, "Size exceeded capacity after inserting key " + i);
        }
    }

    // -------------------------------------------------------------------------
    // Get on non-existent / evicted key
    // -------------------------------------------------------------------------

    @Test
    void get_onEmptyCache_returnsMinusOne() {
        LRUCache cache = new LRUCache(5);
        assertEquals(-1, cache.get(42));
    }

    @Test
    void get_keyNeverInserted_returnsMinusOne() {
        LRUCache cache = new LRUCache(5);
        cache.put(1, 10);
        cache.put(2, 20);

        assertEquals(-1, cache.get(99));
    }

    @Test
    void get_evictedKey_returnsMinusOne() {
        LRUCache cache = new LRUCache(3);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        cache.put(4, 40); // evicts key 1

        assertEquals(-1, cache.get(1));
    }

    // -------------------------------------------------------------------------
    // Null safety
    // -------------------------------------------------------------------------

    @Test
    void put_nullKey_throwsIllegalArgumentException() {
        LRUCache cache = new LRUCache(5);
        assertThrows(IllegalArgumentException.class, () -> cache.put(null, 10));
    }

    @Test
    void put_nullValue_throwsIllegalArgumentException() {
        LRUCache cache = new LRUCache(5);
        assertThrows(IllegalArgumentException.class, () -> cache.put(1, null));
    }

    @Test
    void get_nullKey_throwsIllegalArgumentException() {
        LRUCache cache = new LRUCache(5);
        assertThrows(IllegalArgumentException.class, () -> cache.get(null));
    }

    @Test
    void constructor_zeroCapacity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new LRUCache(0));
    }

    @Test
    void constructor_negativeCapacity_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new LRUCache(-1));
    }

    @Test
    void constructor_nonIntegerUserInput_throwsNumberFormatException() {
        // simulates what happens when user types "abc" as capacity in Main
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("abc"));
    }

    @Test
    void constructor_emptyUserInput_throwsNumberFormatException() {
        // simulates user pressing enter without typing a capacity
        assertThrows(NumberFormatException.class, () -> Integer.parseInt(""));
    }
}
