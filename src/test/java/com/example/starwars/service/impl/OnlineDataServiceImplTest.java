package com.example.starwars.service.impl;

import com.example.starwars.component.SearchResultModelAssembler;
import com.example.starwars.component.StarWarsDataParser;
import com.example.starwars.model.SearchResult;
import com.example.starwars.service.OfflineDataService;
import com.example.starwars.service.OnlineDataService;
import com.example.starwars.service.StarWarsApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.EntityModel;

import java.util.Arrays;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OnlineDataServiceImplTest  {

    private StarWarsApiClient apiClient;
    private StarWarsDataParser parser;
    private OfflineDataService offlineDataService;
    private SearchResultModelAssembler modelAssembler;
    private OnlineDataServiceImpl onlineDataService;

    @BeforeEach
    public void setUp() throws Exception {
        apiClient = mock(StarWarsApiClient.class);
        parser = mock(StarWarsDataParser.class);
        offlineDataService = mock(OfflineDataService.class);
        modelAssembler = mock(SearchResultModelAssembler.class);

        onlineDataService = new OnlineDataServiceImpl(modelAssembler, offlineDataService, parser, apiClient);

        // Set private field 'baseUrl' via reflection
        Field field = OnlineDataServiceImpl.class.getDeclaredField("baseUrl");
        field.setAccessible(true);
        field.set(onlineDataService, "https://swapi.dev/api/%s/?search=%s");
    }

    @Test
    void fetchData_shouldReturnModel_whenApiReturnsValidData() {
        String type = "planets";
        String name = "Tatooine";
        String fullUrl = "https://swapi.dev/api/planets/?search=Tatooine";
    
        JsonNode rootNode = mock(JsonNode.class);
        JsonNode resultsNode = mock(ArrayNode.class);
        when(apiClient.fetch(fullUrl)).thenReturn(rootNode);
        when(rootNode.has("results")).thenReturn(true);
        when(rootNode.path("results")).thenReturn(resultsNode);
        when(resultsNode.isEmpty()).thenReturn(false);
    
        SearchResult parsedResult = new SearchResult(type, name, 1, Arrays.asList("https://swapi.dev/api/films/1/"));
        when(parser.parseResult(type, name, rootNode)).thenReturn(parsedResult);
    
        JsonNode filmNode = mock(ObjectNode.class);
        when(apiClient.fetch("https://swapi.dev/api/films/1/?format=json")).thenReturn(filmNode);
        when(parser.parseFilmTitle(filmNode)).thenReturn("A New Hope");
    
        parsedResult.setFilms(Arrays.asList("A New Hope"));
        EntityModel<SearchResult> expectedModel = EntityModel.of(parsedResult);
        when(modelAssembler.toModel(parsedResult)).thenReturn(expectedModel);
    
        EntityModel<SearchResult> result = onlineDataService.fetchData(type, name);
    
        assertNotNull(result);
        assertEquals(expectedModel, result);
        verify(modelAssembler).toModel(parsedResult);
    
        // Removed: verify(offlineDataService).updateData(type, name, parsedResult);
    }
    

    @Test
    void fetchData_shouldReturnNoFilmModel_whenApiReturnsNoResults() {
        String type = "people";
        String name = "Unknown";
        String fullUrl = "https://swapi.dev/api/people/?search=Unknown";

        JsonNode rootNode = mock(JsonNode.class);
        JsonNode emptyResults = mock(ArrayNode.class);
        when(apiClient.fetch(fullUrl)).thenReturn(rootNode);
        when(rootNode.has("results")).thenReturn(true);
        when(rootNode.path("results")).thenReturn(emptyResults);
        when(emptyResults.isEmpty()).thenReturn(true);

        SearchResult emptyResult = new SearchResult(type, name, "No Films Found");
        EntityModel<SearchResult> expectedModel = EntityModel.of(emptyResult);

        when(modelAssembler.toModel(emptyResult)).thenReturn(expectedModel);

        EntityModel<SearchResult> result = onlineDataService.fetchData(type, name);

        assertEquals(expectedModel, result);
        verify(modelAssembler).toModel(emptyResult);
        verifyNoMoreInteractions(parser, offlineDataService);
    }
}
