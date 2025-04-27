package com.example.starwars.star_wars.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.starwars.star_wars.model.SearchResult;
import com.example.starwars.star_wars.service.SearchService;



@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public SearchResult search(@RequestParam String type, @RequestParam String name) {
        return searchService.search(type, name);
    }
}
