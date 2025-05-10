// package com.example.starwars.controller;

// import com.example.starwars.service.KafkaProducer;
// import com.example.starwars.service.SearchService;
// import com.example.starwars.model.SearchResult;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.listener.MessageListener;
// import org.springframework.kafka.listener.MessageListenerContainer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.kafka.annotation.EnableKafka;
// import org.springframework.hateoas.EntityModel;
// import org.springframework.http.ResponseEntity;



// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @EnableKafka // If needed to enable Kafka configuration for the test
// public class SearchControllerTest {

//     @LocalServerPort
//     private int port;

//     @Autowired
//     private TestRestTemplate restTemplate;

//     @Mock
//     private KafkaProducer kafkaProducer;

//     @Mock
//     private SearchService searchService;

//     @InjectMocks
//     private SearchController searchController;

//     @Mock
//     private KafkaTemplate<String, String> kafkaTemplate; // Mock KafkaTemplate to prevent real Kafka interaction

//     @Test
//     public void testSearch() {
//         // Test parameters
//         String type = "character";
//         String name = "Luke Skywalker";
//         Boolean offlineMode = false;
//         kafkaTemplate.send("test-topic", "Hello Kafka");

//         // Mock the behavior of searchService
//         SearchResult mockSearchResult = new SearchResult();
//         mockSearchResult.setName("Luke Skywalker");
//         mockSearchResult.setType("character");
//         when(searchService.search(type, name, offlineMode)).thenReturn(EntityModel.of(mockSearchResult));
        

//         // Construct the URL with query parameters
//         String url = "http://localhost:" + port + "/search?type=" + type + "&name=" + name + "&offlineMode=" + offlineMode;

//         // Send GET request to the search endpoint
//         ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

//         // Assert that the response status code is 200 OK
//         assertEquals(200, response.getStatusCodeValue());

//         // Verify that KafkaProducer's sendSearchRequest() method was called once
//         verify(kafkaProducer, times(1)).sendSearchRequest(type, name, offlineMode);
//     }
// }




