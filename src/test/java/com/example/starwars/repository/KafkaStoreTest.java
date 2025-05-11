package com.example.starwars.repository;


import com.example.starwars.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

class KafkaStoreTest {

    private KafkaStore kafkaStore;

    @BeforeEach
    void setUp() {
        kafkaStore = new KafkaStore();
    }

    @Test
    void updateAndGet_shouldStoreAndRetrieveCorrectResult() {
        String requestID = "1234";
        SearchResult searchResult = new SearchResult("people", "Luke Skywalker", "A New Hope");
        EntityModel<SearchResult> model = EntityModel.of(searchResult);

        kafkaStore.update(requestID, model);

        EntityModel<SearchResult> retrieved = kafkaStore.get(requestID);

        assertNotNull(retrieved);
        assertEquals(model, retrieved);
        assertEquals("Luke Skywalker", retrieved.getContent().getName());
    }

    @Test
    void get_shouldReturnNullForUnknownRequestID() {
        EntityModel<SearchResult> result = kafkaStore.get("non-existent-id");
        assertNull(result);
    }

    @Test
    void clearCache_shouldRemoveAllEntries() {
        kafkaStore.update("1", EntityModel.of(new SearchResult("planets", "Tatooine", "A New Hope")));
        kafkaStore.update("2", EntityModel.of(new SearchResult("people", "Leia Organa", "A New Hope")));

        kafkaStore.clearCache();

        assertNull(kafkaStore.get("1"));
        assertNull(kafkaStore.get("2"));
    }
}