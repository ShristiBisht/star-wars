package com.example.starwars.service;


import org.springframework.hateoas.EntityModel;

import com.example.starwars.model.SearchResult;


public interface OnlineDataService {
    EntityModel<SearchResult> fetchData(String type, String name) ;
    
}


