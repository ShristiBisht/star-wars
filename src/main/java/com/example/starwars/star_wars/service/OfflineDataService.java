package com.example.starwars.star_wars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.starwars.star_wars.model.SearchResult;
import com.example.starwars.star_wars.repository.InMemoryCache;



@Service
public class OfflineDataService {

    @Autowired
    private InMemoryCache inMemoryCache;

    public SearchResult fetchData(String type, String name) {
        return inMemoryCache.get(type, name);
    }
}
