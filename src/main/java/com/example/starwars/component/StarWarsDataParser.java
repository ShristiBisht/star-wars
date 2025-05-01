package com.example.starwars.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.starwars.model.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class StarWarsDataParser {

    public SearchResult parseResult(String type, String name, JsonNode rootNode) {
        int count = rootNode.path("count").asInt();
        JsonNode firstResult = rootNode.path("results").get(0);
        String entityName = firstResult.path("name").asText();
        List<String> filmUrls = new ArrayList<>();

        if (firstResult.has("films")) {
            for (JsonNode filmNode : firstResult.path("films")) {
                filmUrls.add(filmNode.asText());
            }
        }

        return new SearchResult(type, entityName, count, filmUrls);
    }

    public String parseFilmTitle(JsonNode filmNode) {
        return filmNode.path("title").asText();
    }
}
