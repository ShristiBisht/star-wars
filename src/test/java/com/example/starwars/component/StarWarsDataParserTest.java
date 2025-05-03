package com.example.starwars.component;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.starwars.model.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class StarWarsDataParserTest {

    @Test
    void testParseResult() {
        StarWarsDataParser parser = new StarWarsDataParser();

        // Mock JsonNode for testing
        JsonNode rootNode = mock(JsonNode.class);
        JsonNode resultsNode = mock(JsonNode.class);
        JsonNode firstResultNode = mock(JsonNode.class);
        JsonNode filmsNode = mock(JsonNode.class);

        when(rootNode.path("count")).thenReturn(mock(JsonNode.class));
        when(rootNode.path("results")).thenReturn(resultsNode);
        when(resultsNode.get(0)).thenReturn(firstResultNode);
        when(firstResultNode.path("name")).thenReturn(mock(JsonNode.class));
        when(firstResultNode.path("films")).thenReturn(filmsNode);

        // Test mock behaviors
        when(firstResultNode.path("name").asText()).thenReturn("Millennium Falcon");
        when(filmsNode.isArray()).thenReturn(true);

        // Create a SearchResult object based on the mock data
        SearchResult result = parser.parseResult("starship", "Millennium Falcon", rootNode);

        assertEquals("starship", result.getType());
        assertEquals(0, result.getCount());  // Mocking count value
        assertNotNull(result.getFilms());
    }

    @Test
    void testParseFilmTitle() {
        StarWarsDataParser parser = new StarWarsDataParser();

        JsonNode filmNode = mock(JsonNode.class);
        when(filmNode.path("title")).thenReturn(mock(JsonNode.class));

        String title = parser.parseFilmTitle(filmNode);

        assertEquals("Unknown Film Title", title);
    }
}