package com.example.starwars.service;

import org.springframework.hateoas.EntityModel;

import com.example.starwars.model.SearchResult;

public interface SearchService {
    EntityModel<SearchResult> search(String type, String name);
    
    void updateSearchData(String type, String name, SearchResult updatedData);

    void evictAllSearchResultsCache();
}
