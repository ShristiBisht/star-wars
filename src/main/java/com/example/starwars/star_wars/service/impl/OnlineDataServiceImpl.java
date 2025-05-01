package com.example.starwars.star_wars.service.impl;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.example.starwars.star_wars.component.SearchResultModelAssembler;
import com.example.starwars.star_wars.component.StarWarsDataParser;
import com.example.starwars.star_wars.model.SearchResult;
import com.example.starwars.star_wars.service.OfflineDataService;
import com.example.starwars.star_wars.service.OnlineDataService;
import com.example.starwars.star_wars.service.StarWarsApiClient;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class OnlineDataServiceImpl implements OnlineDataService {

    @Autowired
    private StarWarsApiClient starWarsApiClient;

    @Autowired
    private StarWarsDataParser parser;

    @Autowired
    private OfflineDataService offlineDataService;

    @Autowired
    private SearchResultModelAssembler modelAssembler;

    @Value("${starwars.base-url}")
    private String baseUrl ;
    
    public EntityModel<SearchResult> fetchData(String type, String name) {
        String url = String.format(baseUrl, type, URLEncoder.encode(name, StandardCharsets.UTF_8));

        JsonNode rootNode = starWarsApiClient.fetch(url);
        if (rootNode == null || !rootNode.has("results") || rootNode.path("results").isEmpty()) {
            return modelAssembler.toModel(new SearchResult(type, name, "No Films Found"));
        }

        SearchResult result = parser.parseResult(type, name, rootNode);

        List<String> filmTitles = new ArrayList<>();
        for (String filmUrl : result.getFilms()) {
            JsonNode filmNode = starWarsApiClient.fetch(filmUrl + "?format=json");
            if (filmNode != null) {
                filmTitles.add(parser.parseFilmTitle(filmNode));
            }
        }

        result.setFilms(filmTitles);
        offlineDataService.updateData(type, name, result);

        return modelAssembler.toModel(result);
    }
}

