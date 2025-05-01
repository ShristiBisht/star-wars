package com.example.starwars.star_wars.repository;

import org.springframework.stereotype.Repository;

import com.example.starwars.star_wars.model.SearchResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Repository
public class InMemoryCache {
    Logger logger = Logger.getLogger("InMemoryCache.class");

    private ConcurrentHashMap<String, SearchResult> cache = new ConcurrentHashMap<>();

    public void update(String type, String name, SearchResult result) {
        cache.put(type + "-" + name, result);
        logger.info("Cache items are : "+cache);
    }

    public SearchResult get(String type, String name) {
        return cache.get(type + "-" + name);
    }
}
