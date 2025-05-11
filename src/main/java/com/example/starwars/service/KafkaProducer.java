package com.example.starwars.service;

import org.apache.kafka.common.protocol.types.Field.Str;
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
    public void sendSearchRequest(String type, String name, String requestID) {
        String message = type + "|" + name + "|" + requestID ;
        kafkaTemplate.send(TOPIC, message);
    }
}
