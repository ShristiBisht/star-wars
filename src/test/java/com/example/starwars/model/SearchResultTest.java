package com.example.starwars.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SearchResultTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        List<String> films = List.of("A New Hope", "Empire Strikes Back");
        SearchResult result = new SearchResult("Planet", "Tatooine", 2, films);

        assertEquals("Planet", result.getType());
        assertEquals("Tatooine", result.getName());
        assertEquals(2, result.getCount());
        assertEquals(films, result.getFilms());
    }

    @Test
    void testCustomConstructorWithSingleFilm() {
        SearchResult result = new SearchResult("Vehicle", "Sand Crawler", "A New Hope");

        assertEquals("Vehicle", result.getType());
        assertEquals("Sand Crawler", result.getName());
        assertEquals(List.of("A New Hope"), result.getFilms());
    }


    @Test
    void testNoArgsConstructorAndSetters() {
        SearchResult result = new SearchResult();
        result.setType("Spaceship");
        result.setName("Millennium Falcon");
        result.setCount(1);
        result.setFilms(List.of("Return of the Jedi"));
        result.setOfflineMode(true);

        assertEquals("Spaceship", result.getType());
        assertEquals("Millennium Falcon", result.getName());
        assertEquals(1, result.getCount());
        assertEquals(List.of("Return of the Jedi"), result.getFilms());
        assertTrue(result.getOfflineMode());
    }

    @Test
    void testToString() {
        SearchResult result = new SearchResult("Character", "Luke Skywalker", 1, List.of("A New Hope", "Empire Strikes Back"));
        String expected = "SearchResult{type='Character', name='Luke Skywalker', count=1', films=[A New Hope, Empire Strikes Back]', offline Mode=null}";
        assertEquals(expected, result.toString());
    }
}
