package com.example.starwars.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Import your controller here for WebMvcTest to scan
import com.example.starwars.controller.SearchController;
import com.example.starwars.service.SearchService;
import com.example.starwars.service.KafkaProducer;

@WebMvcTest(controllers = SearchController.class)
@Import(SecurityConfig.class)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // These beans are required by SearchController
    @MockBean
    private SearchService searchService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void shouldPermitAccessToSearchEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/search")
                .param("type", "planets")
                .param("name", "Tatooine"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldAllowFormLoginEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
            .andExpect(status().isOk()); // You might get a redirect (302) depending on your config
    }

    @Test
    void shouldAllowLogoutEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/logout"))
            .andExpect(status().is3xxRedirection());
    }
}
