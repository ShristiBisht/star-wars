package com.example.starwars.config;

import java.util.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    Logger logger = Logger.getLogger("AppConfig.class");

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();


        return restTemplate;
    }
}
