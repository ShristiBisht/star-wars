package com.example.starwars.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig
@Import(AppConfig.class)
class AppConfigTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void restTemplateBeanShouldBeCreated() {
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
    }
}
