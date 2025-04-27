package com.example.starwars.star_wars.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "user-queries";

    public void publishQueryEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
