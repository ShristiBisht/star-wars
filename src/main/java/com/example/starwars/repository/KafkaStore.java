package com.example.starwars.repository;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Repository;

import com.example.starwars.model.SearchResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Repository
public class KafkaStore {

    Logger logger = Logger.getLogger("KafkaStore.class");

    private ConcurrentHashMap<String, EntityModel<SearchResult>> response = new ConcurrentHashMap<>();

    public void update(String requestID, EntityModel<SearchResult> searchResult) {
        response.put(requestID, searchResult);
        logger.info("UUID items are : "+response);
    }

    public EntityModel<SearchResult> get(String requestID) {
        return response.get(requestID);
    }

    // Optional: clear cache periodically or based on specific conditions
    public void clearCache() {
        response.clear();
    }
    
}
    
