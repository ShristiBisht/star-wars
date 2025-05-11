package com.example.starwars.service;


import com.example.starwars.model.SearchResult;
import com.example.starwars.repository.KafkaStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class KafkaConsumer{

    private SearchService searchService;
    private KafkaStore kafkaStore;
    private Logger logger = Logger.getLogger(KafkaConsumer.class.getName());

    @Autowired
    public KafkaConsumer( SearchService searchService, KafkaStore kafkaStore){
        this.searchService=searchService;
        this.kafkaStore=kafkaStore;
    }


    @KafkaListener(topics = "search-requests", groupId = "star-wars-group")
    public void consume(String message) {
        logger.info("Received Kafka message: " + message);
        String[] parts = message.split("\\|");
        if (parts.length != 3) return;

        String type = parts[0];
        String name = parts[1];
        String requestID = parts[2];
        EntityModel<SearchResult> result = searchService.search(type, name);
        kafkaStore.update(requestID, result);
        logger.info("Result from Kafka consumer: " + kafkaStore.get(requestID));
    }
}
