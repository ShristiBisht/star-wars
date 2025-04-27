package com.example.starwars.star_wars.config;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    Logger logger = Logger.getLogger("AppConfig.class");

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Get the list of existing converters
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        logger.info("Converter : "+converters);
        
        // Add Jackson message converter for JSON
        converters.add(new MappingJackson2HttpMessageConverter());
        logger.info("Converter  New : "+converters);
        logger.info("Rest Template is  : "+restTemplate);


        return restTemplate;
    }
}
