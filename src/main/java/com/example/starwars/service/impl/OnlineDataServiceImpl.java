package com.example.starwars.service.impl;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.example.starwars.component.SearchResultModelAssembler;
import com.example.starwars.component.StarWarsDataParser;
import com.example.starwars.model.SearchResult;
import com.example.starwars.service.OfflineDataService;
import com.example.starwars.service.OnlineDataService;
import com.example.starwars.service.StarWarsApiClient;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class OnlineDataServiceImpl implements OnlineDataService {

    private StarWarsApiClient starWarsApiClient;
    private StarWarsDataParser parser;
    private OfflineDataService offlineDataService;
    private SearchResultModelAssembler modelAssembler;
    private static final Logger logger = Logger.getLogger(OnlineDataServiceImpl.class.getName());


    @Autowired
    public OnlineDataServiceImpl(SearchResultModelAssembler modelAssembler,OfflineDataService offlineDataService, StarWarsDataParser parser, StarWarsApiClient starWarsApiClient) {
        this.starWarsApiClient=starWarsApiClient;
        this.parser=parser;
        this.offlineDataService= offlineDataService;
        this.modelAssembler=modelAssembler;
  }
    @Value("${starwars.base-url}")
    private String baseUrl ;
    
    public EntityModel<SearchResult> fetchData(String type, String name) {
        String url = String.format(baseUrl, type, URLEncoder.encode(name, StandardCharsets.UTF_8));
        JsonNode rootNode = starWarsApiClient.fetch(url);
        if (rootNode == null || !rootNode.has("results") || rootNode.path("results").isEmpty()) {
            return modelAssembler.toModel(new SearchResult(type, name, "No Films Found"));
        }

        SearchResult result = parser.parseResult(type, name, rootNode);
        logger.info("Filmy results "+result);
        if(result.getName().equalsIgnoreCase(name) || result.getType().equals("films")){
            List<String> filmTitles = new ArrayList<>();
            for (String filmUrl : result.getFilms()) {
                JsonNode filmNode = starWarsApiClient.fetch(filmUrl + "?format=json");
                if (filmNode != null) {
                    filmTitles.add(parser.parseFilmTitle(filmNode));
                }
            }

            result.setFilms(filmTitles);
            // offlineDataService.updateData(type, name, result);

            return modelAssembler.toModel(result);
        }
        return modelAssembler.toModel(new SearchResult(type, name, "Nothing Found"));
    }
}

