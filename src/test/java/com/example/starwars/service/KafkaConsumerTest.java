package com.example.starwars.service;

import com.example.starwars.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.EntityModel;

import java.util.Collections;

import static org.mockito.Mockito.*;

class KafkaConsumerTest {

    private KafkaConsumer kafkaConsumer;
    private SearchService mockSearchService;

    @BeforeEach
    void setUp() {
        mockSearchService = mock(SearchService.class);
        kafkaConsumer = new KafkaConsumer(mockSearchService);
    }

    @Test
    void consume_shouldCallSearchServiceWithValidMessage() {
        // given
        String message = "people|Luke Skywalker";
        SearchResult dummyResult = mock(SearchResult.class);
        EntityModel<SearchResult> dummyModel = EntityModel.of(dummyResult);

        when(mockSearchService.search("people", "Luke Skywalker")).thenReturn(dummyModel);

        // when
        kafkaConsumer.consume(message);

        // then
        verify(mockSearchService, times(1)).search("people", "Luke Skywalker");
    }

    @Test
    void consume_shouldNotCallSearchService_whenInvalidMessageFormat() {
        kafkaConsumer.consume("invalid-message");

        verify(mockSearchService, never()).search(anyString(), anyString());
    }
}
