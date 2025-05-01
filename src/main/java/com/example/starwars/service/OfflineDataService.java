package com.example.starwars.service;

import com.example.starwars.model.SearchResult;

public interface OfflineDataService {
    SearchResult fetchData(String type, String name);
    void updateData(String type, String name, SearchResult result);
    void evictSearchResultFromCache(String type, String name);
}
