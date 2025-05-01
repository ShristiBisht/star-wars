package com.example.starwars.star_wars.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface StarWarsApiClient {
    JsonNode fetch(String endpointUrl);
}
