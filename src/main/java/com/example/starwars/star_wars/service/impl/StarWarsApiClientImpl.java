package com.example.starwars.star_wars.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.starwars.star_wars.service.StarWarsApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


@Service
public class StarWarsApiClientImpl implements StarWarsApiClient {

    private static final Logger logger = LoggerFactory.getLogger(StarWarsApiClientImpl.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public JsonNode fetch(String endpointUrl) {
        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            //  TODO: getting called 5 times, need to check
            logger.info("HTTP Response: {}", responseCode);

            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    return objectMapper.readTree(response.toString());
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching data from: {}", endpointUrl, e);
        }
        return null;
    }
}
