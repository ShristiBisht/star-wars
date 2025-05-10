package com.example.starwars.repository;

import com.example.starwars.model.SearchResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCacheTest {

    @Test
    void testUpdateAndGet() {
        InMemoryCache cache = new InMemoryCache();
        SearchResult result = new SearchResult("planets", "Tatooine", 3, null);
        cache.update("planets", "Tatooine", result);

        SearchResult cached = cache.get("planets", "Tatooine");
        assertNotNull(cached);
        assertEquals("Tatooine", cached.getName());
    }

    @Test
    void testGetReturnsNullForMissingKey() {
        InMemoryCache cache = new InMemoryCache();
        assertNull(cache.get("planets", "Unknown"));
    }
}
