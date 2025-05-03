package com.example.starwars.component;

import com.example.starwars.model.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StarWarsDataParserTest {

    private final StarWarsDataParser parser = new StarWarsDataParser();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testParseResult_withValidJson_shouldReturnParsedResult() throws IOException {
        String json = """
            {
              "count": 1,
              "results": [
                {
                  "name": "Luke Skywalker",
                  "films": [
                    "https://swapi.dev/api/films/1/",
                    "https://swapi.dev/api/films/2/"
                  ]
                }
              ]
            }
            """;

        JsonNode rootNode = objectMapper.readTree(json);
        SearchResult result = parser.parseResult("people", "Luke", rootNode);

        assertEquals("people", result.getType());
        assertEquals("Luke Skywalker", result.getName());
        assertEquals(1, result.getCount());
        assertEquals(List.of(
            "https://swapi.dev/api/films/1/",
            "https://swapi.dev/api/films/2/"), result.getFilms());
    }

    @Test
    void testParseResult_withMissingNameAndFilms_shouldReturnDefaults() throws IOException {
        String json = """
            {
              "count": 0,
              "results": [
                {}
              ]
            }
            """;

        JsonNode rootNode = objectMapper.readTree(json);
        SearchResult result = parser.parseResult("people", "Unknown", rootNode);

        assertEquals("people", result.getType());
        assertEquals("Unknown Entity", result.getName());
        assertEquals(0, result.getCount());
        assertTrue(result.getFilms().isEmpty());
    }

    @Test
    void testParseFilmTitle_withValidTitle_shouldReturnTitle() throws IOException {
        String json = """
            {
              "title": "A New Hope"
            }
            """;

        JsonNode filmNode = objectMapper.readTree(json);
        String title = parser.parseFilmTitle(filmNode);

        assertEquals("A New Hope", title);
    }

    @Test
    void testParseFilmTitle_withMissingTitle_shouldReturnDefault() throws IOException {
        String json = "{}";

        JsonNode filmNode = objectMapper.readTree(json);
        String title = parser.parseFilmTitle(filmNode);

        assertEquals("Unknown Film Title", title);
    }
}
