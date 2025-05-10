package com.example.starwars.service;


import com.example.starwars.model.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaConsumer{

    private SearchService searchService;
    private Logger logger = Logger.getLogger(KafkaConsumer.class.getName());

    @Autowired
    public KafkaConsumer( SearchService searchService){
        this.searchService=searchService;
    }


    @KafkaListener(topics = "search-requests", groupId = "star-wars-group")
    public void consume(String message) {
        logger.info("Received Kafka message: " + message);
        String[] parts = message.split("\\|");
        if (parts.length != 2) return;

        String type = parts[0];
        String name = parts[1];
        EntityModel<SearchResult> result = searchService.search(type, name);
        logger.info("Result from Kafka consumer: " + result.getContent());
    }
}
