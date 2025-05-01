package com.example.starwars.star_wars.service;


import com.example.starwars.star_wars.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.starwars.star_wars.model.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaConsumer{

    @Autowired
    private SearchService searchService;

    private Logger logger = Logger.getLogger(KafkaConsumer.class.getName());

    @KafkaListener(topics = "search-requests", groupId = "star-wars-group")
    public void consume(String message) {
        logger.info("Received Kafka message: " + message);
        String[] parts = message.split("\\|");
        if (parts.length != 3) return;

        String type = parts[0];
        String name = parts[1];
        boolean offlineMode = Boolean.parseBoolean(parts[2]);

        EntityModel<SearchResult> result = searchService.search(type, name, offlineMode);
        logger.info("Result from Kafka consumer: " + result.getContent());
    }
}
