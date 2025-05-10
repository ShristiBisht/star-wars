package com.example.starwars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String TOPIC = "search-requests";

    
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }
    public void sendSearchRequest(String type, String name) {
        String message = type + "|" + name ;
        kafkaTemplate.send(TOPIC, message);
    }
}
