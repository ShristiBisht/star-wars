package com.example.starwars.service.impl;

import com.example.starwars.model.SearchResult;
import com.example.starwars.repository.InMemoryCache;
import com.example.starwars.service.OfflineDataService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfflineDataServiceImplTest {

    private InMemoryCache inMemoryCache;
    private OfflineDataService offlineDataService;

    @BeforeEach
    void setUp() {
        inMemoryCache = mock(InMemoryCache.class);
        offlineDataService = new OfflineDataServiceImpl(inMemoryCache);
    }

    @Test
    void fetchData_shouldReturnData_whenCacheHit() {
        // Given
        SearchResult expectedResult = new SearchResult("planets", "Tatooine", 3, null);
        when(inMemoryCache.get("planets", "Tatooine")).thenReturn(expectedResult);

        // When
        SearchResult actualResult = offlineDataService.fetchData("planets", "Tatooine");

        // Then
        assertNotNull(actualResult);
        assertEquals("Tatooine", actualResult.getName());
        verify(inMemoryCache, times(1)).get("planets", "Tatooine");
    }

    @Test
    void fetchData_shouldReturnNull_whenCacheMiss() {
        // Given
        when(inMemoryCache.get("starships", "Falcon")).thenReturn(null);

        // When
        SearchResult result = offlineDataService.fetchData("starships", "Falcon");

        // Then
        assertNull(result);
        verify(inMemoryCache).get("starships", "Falcon");
    }

    @Test
    void updateData_shouldCallInMemoryCacheUpdate() {
        // Given
        SearchResult result = new SearchResult("films", "A New Hope", 1, null);

        // When
        offlineDataService.updateData("films", "A New Hope", result);

        // Then
        verify(inMemoryCache, times(1)).update("films", "A New Hope", result);
    }

    @Test
    void evictSearchResultFromCache_shouldJustLog() {
        // Since the method only logs, we just ensure it runs without exceptions
        assertDoesNotThrow(() -> offlineDataService.evictSearchResultFromCache("people", "Luke Skywalker"));
    }
}
