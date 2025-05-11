package com.example.starwars.controller;



import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.starwars.model.SearchResult;
import com.example.starwars.repository.KafkaStore;
import com.example.starwars.service.KafkaProducer;
import com.example.starwars.service.SearchService;


@RestController
public class SearchController {

    
    private KafkaProducer kafkaProducer;
    private SearchService searchService;
    private KafkaStore kafkaStore;

    Logger logger = Logger.getLogger("SearchController.class");

    @Autowired
    public void SearchController(KafkaProducer kafkaProducer,SearchService searchService,KafkaStore kafkaStore ){
        this.kafkaProducer=kafkaProducer;
        this.searchService=searchService;  
        this.kafkaStore=kafkaStore; 
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/search")
    public ResponseEntity<ConcurrentHashMap<String, String>> search(@RequestParam String type, @RequestParam String name) {
        logger.info("Sending message to Kafka...");
        String requestId = UUID.randomUUID().toString();
        kafkaProducer.sendSearchRequest(type, name,requestId);
        // Return the requestId to the frontend for polling
        ConcurrentHashMap<String, String> response = new ConcurrentHashMap<>();
        response.put("requestId", requestId);
        return ResponseEntity.ok(response);
    }

    // Endpoint to poll for search result
    @GetMapping("/result/{requestId}")
    public ResponseEntity<EntityModel<SearchResult>> getSearchResult(@PathVariable String requestId) {
        EntityModel<SearchResult> result = kafkaStore.get(requestId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                 .body(null);  // No result yet
        }
        return ResponseEntity.ok(result);  // Return the result if available
    }

    // Endpoint for updating data (and evicting the cache)
    @PostMapping("/updateSearchData")
    public void updateSearchData(@RequestParam String type, @RequestParam String name, @RequestBody SearchResult updatedData) {
        logger.info("Updating search data for: " + type + " - " + name);
        searchService.updateSearchData(type, name, updatedData);
    }

    // Endpoint to evict all search results cache (optional)
    @PostMapping("/evictAllSearchResultsCache")
    public void evictAllSearchResultsCache() {
        logger.info("Evicting all search results cache");
        searchService.evictAllSearchResultsCache();
    }

    }

