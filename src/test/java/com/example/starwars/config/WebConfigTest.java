package com.example.starwars.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WebConfig.class) // Loads only MVC-related beans
@Import(WebConfig.class) // Import the config you want to test
public class WebConfigTest {

@Autowired
private MockMvc mockMvc;

@Test
void corsConfigurationShouldAllowLocalhost3000Origin() throws Exception {
    mockMvc.perform(
            options("/some-endpoint")
                    .header("Origin", "http://localhost:3000")
                    .header("Access-Control-Request-Method", "GET"));
    }
}
