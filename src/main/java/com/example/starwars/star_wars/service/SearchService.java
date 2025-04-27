package com.example.starwars.star_wars.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.starwars.star_wars.model.SearchResult;



@Service
public class SearchService {

    @Autowired
    private OnlineDataService onlineDataService;
    
    @Autowired
    private OfflineDataService offlineDataService;

    public SearchResult search(String type, String name) {
        SearchResult result = onlineDataService.fetchData(type, name);

        if (result == null) {
            // Fallback to offline data
            result = offlineDataService.fetchData(type, name);
        }

        return result;
    }
}
