package com.example.starwars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

class KafkaProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaProducer = new KafkaProducer(kafkaTemplate);
    }

    @Test
    void sendSearchRequest_shouldSendMessageToKafka() {
        // given
        String type = "people";
        String name = "Luke Skywalker";
        String requestID="abcd";
        String expectedMessage = "people|Luke Skywalker|abcd";

        // when
        kafkaProducer.sendSearchRequest(type, name,requestID);

        // then
        verify(kafkaTemplate, times(1)).send("search-requests", expectedMessage);
    }
}
