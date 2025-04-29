package com.example.starwars.star_wars.service;

import com.example.starwars.star_wars.model.SearchResult;

public interface OfflineDataService {
    SearchResult fetchData(String type, String name);
    void updateData(String type, String name, SearchResult result);
    void evictSearchResultFromCache(String type, String name);
}
