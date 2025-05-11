package com.example.starwars.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.starwars.model.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class StarWarsDataParser {

    // Utility method to safely extract a String from a JsonNode with a default value
    private String getSafeText(JsonNode node, String fieldName, String defaultValue) {
        if (node.has(fieldName)) {
            return node.path(fieldName).asText(defaultValue);
        }
        return defaultValue;
    }

    // Utility method to extract a List of Strings from a JsonNode (for films)
    private List<String> getFilms(JsonNode filmsNode) {
        List<String> filmUrls = new ArrayList<>();
        if (filmsNode != null && filmsNode.isArray()) {
            filmsNode.forEach(filmNode -> filmUrls.add(filmNode.asText()));
        }
        return filmUrls;
    }

    // Parses the search result data
    public SearchResult parseResult(String type, String name, JsonNode rootNode) {
        int count = rootNode.path("count").asInt();
        JsonNode firstResult = rootNode.path("results").get(0);

        // Use the utility methods to get the data
        String entityName = type.equalsIgnoreCase("films")?getSafeText(firstResult, "title", "Unknown Entity"):getSafeText(firstResult, "name", "Unknown Entity");
        List<String> filmUrls = type.equalsIgnoreCase("films")?Collections.singletonList(entityName):getFilms(firstResult.path("films"));

        return new SearchResult(type, entityName, count, filmUrls);
    }

    // Parses the film title from a JsonNode
    public String parseFilmTitle(JsonNode filmNode) {
        return getSafeText(filmNode, "title", "Unknown Film Title");
    }
}
