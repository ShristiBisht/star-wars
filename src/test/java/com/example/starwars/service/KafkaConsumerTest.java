package com.example.starwars.service;

import com.example.starwars.model.SearchResult;
import com.example.starwars.repository.KafkaStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.mockito.Mockito.*;

class KafkaConsumerTest {

    private KafkaConsumer kafkaConsumer;
    private SearchService searchService;
    private KafkaStore kafkaStore;

    @BeforeEach
    void setUp() {
        searchService = mock(SearchService.class);
        kafkaStore = mock(KafkaStore.class);
        kafkaConsumer = new KafkaConsumer(searchService, kafkaStore);
    }

    @Test
    void consume_shouldCallSearchServiceWithValidMessage() {
        // given
        String message = "people|Luke Skywalker|1234";
        SearchResult dummyResult = mock(SearchResult.class);
        EntityModel<SearchResult> dummyModel = EntityModel.of(dummyResult);

        when(searchService.search("people", "Luke Skywalker")).thenReturn(dummyModel);

        // when
        kafkaConsumer.consume(message);

        // then
        verify(searchService, times(1)).search("people", "Luke Skywalker");
        verify(kafkaStore, times(1)).update("1234", dummyModel);
    }

    @Test
    void consume_shouldNotCallSearchService_whenInvalidMessageFormat() {
        // given: invalid message (only 1 part)
        kafkaConsumer.consume("invalid-message");

        // then
        verify(searchService, never()).search(anyString(), anyString());
        verify(kafkaStore, never()).update(anyString(), any());
    }
}
