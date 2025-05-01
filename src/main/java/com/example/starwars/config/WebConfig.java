package com.example.starwars.config;

import java.util.logging.Logger;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    Logger logger = Logger.getLogger("WebConfig.class");
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("CORS authentication");
        registry.addMapping("/**")  // Allow CORS for all routes
                .allowedOrigins("http://localhost:3000")  // Your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
