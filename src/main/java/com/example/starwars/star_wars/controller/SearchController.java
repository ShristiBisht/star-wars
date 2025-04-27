package com.example.starwars.star_wars.controller;



import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.starwars.star_wars.model.SearchResult;
import com.example.starwars.star_wars.service.SearchService;



@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;
    Logger logger = Logger.getLogger("SearchController.class");

    @GetMapping("/search")
    public SearchResult search(@RequestParam String type, @RequestParam String name) {
        logger.info("We are getting here");
        return searchService.search(type, name);
    }
}
