package com.example.starwars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String TOPIC = "search-requests";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendSearchRequest(String type, String name, boolean offlineMode) {
        String message = type + "|" + name + "|" + offlineMode;
        kafkaTemplate.send(TOPIC, message);
    }
}
