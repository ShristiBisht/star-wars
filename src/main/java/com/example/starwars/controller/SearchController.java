package com.example.starwars.controller;



import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.starwars.model.SearchResult;
import com.example.starwars.service.KafkaProducer;
import com.example.starwars.service.SearchService;



@RestController
public class SearchController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private SearchService searchService;
    Logger logger = Logger.getLogger("SearchController.class");

    @GetMapping("/search")
    public EntityModel<SearchResult> search(@RequestParam String type, @RequestParam String name, @RequestParam Boolean offlineMode) {
        logger.info("Sending message to Kafka...");
        kafkaProducer.sendSearchRequest(type, name, offlineMode);
        return searchService.search(type, name, offlineMode);
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

