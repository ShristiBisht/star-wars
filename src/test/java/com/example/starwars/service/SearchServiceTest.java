package com.example.starwars.service;

import com.example.starwars.component.SearchResultModelAssembler;
import com.example.starwars.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchServiceTest {

    @Mock
    private OnlineDataService onlineDataService;

    @Mock
    private OfflineDataService offlineDataService;

    @Mock
    private SearchResultModelAssembler assembler;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearch_OnlineReturnsResult() {
        String type = "people";
        String name = "Luke";

        SearchResult mockResult = new SearchResult();
        mockResult.setName(name);

        when(onlineDataService.fetchData(type, name)).thenReturn(EntityModel.of(mockResult));
        when(assembler.toModel(mockResult)).thenReturn(EntityModel.of(mockResult));

        EntityModel<SearchResult> result = searchService.search(type, name);

        assertNotNull(result);
        assertEquals(name, result.getContent().getName());
        verify(offlineDataService, never()).fetchData(anyString(), anyString());
    }

    

    @Test
    public void testSearch_FilmType_Enrichment() {
        String type = "films";
        String name = "Empire Strikes Back";

        SearchResult mockResult = new SearchResult();

        when(onlineDataService.fetchData(type, name)).thenReturn(EntityModel.of(mockResult));
        when(assembler.toModel(mockResult)).thenReturn(EntityModel.of(mockResult));

        EntityModel<SearchResult> result = searchService.search(type, name);

        assertEquals(name, result.getContent().getName());
        assertEquals(1, result.getContent().getFilms().size());
        assertTrue(result.getContent().getFilms().contains(name));
    }
}
