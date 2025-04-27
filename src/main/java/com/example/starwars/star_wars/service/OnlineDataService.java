package com.example.starwars.star_wars.service;

import org.springframework.stereotype.Service;

import com.example.starwars.star_wars.model.SearchResult;


@Service
public class OnlineDataService {

    public SearchResult fetchData(String type, String name) {
        // Fetch data from SWAPI here
        // If API fails, return null
        return new SearchResult(type, name, "Some film");
    }
}
