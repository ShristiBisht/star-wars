package com.example.starwars.star_wars.service;


import org.springframework.hateoas.EntityModel;
import com.example.starwars.star_wars.model.SearchResult;


public interface OnlineDataService {
    EntityModel<SearchResult> fetchData(String type, String name) ;
    
}


