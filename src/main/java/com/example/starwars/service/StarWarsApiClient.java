package com.example.starwars.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface StarWarsApiClient {
    JsonNode fetch(String endpointUrl);
}
