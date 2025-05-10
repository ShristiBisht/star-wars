package com.example.starwars.service.impl;

import com.example.starwars.service.StarWarsApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarWarsApiClientImplTest {

    private MockWebServer mockWebServer;
    private StarWarsApiClient apiClient;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        apiClient = new StarWarsApiClientImpl(); // No mocking needed
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void fetch_shouldReturnJsonNode_whenValidResponse() {
        // given
        String json = "{\"results\":[{\"name\":\"Luke Skywalker\"}]}";
        mockWebServer.enqueue(new MockResponse()
                .setBody(json)
                .addHeader("Content-Type", "application/json"));

        String endpoint = mockWebServer.url("/people").toString();

        // when
        JsonNode response = apiClient.fetch(endpoint);

        // then
        assertNotNull(response);
        assertTrue(response.has("results"));
        assertEquals("Luke Skywalker", response.get("results").get(0).get("name").asText());
    }

    @Test
    void fetch_shouldReturnNull_whenHttpError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
        String endpoint = mockWebServer.url("/notfound").toString();

        JsonNode result = apiClient.fetch(endpoint);

        assertNull(result);
    }
}
