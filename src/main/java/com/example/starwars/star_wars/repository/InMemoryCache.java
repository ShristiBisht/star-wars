package com.example.starwars.star_wars.repository;

import org.springframework.stereotype.Repository;

import com.example.starwars.star_wars.model.SearchResult;

import java.util.Map;
import java.util.HashMap;

@Repository
public class InMemoryCache {

    private Map<String, SearchResult> cache = new HashMap<>();

    public void update(String type, String name, SearchResult result) {
        cache.put(type + "-" + name, result);
    }

    public SearchResult get(String type, String name) {
        return cache.get(type + "-" + name);
    }
}
