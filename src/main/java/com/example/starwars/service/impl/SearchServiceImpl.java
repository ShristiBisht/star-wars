package com.example.starwars.service.impl;

import java.util.Collections;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.example.starwars.component.SearchResultModelAssembler;
import com.example.starwars.model.SearchResult;
import com.example.starwars.service.OfflineDataService;
import com.example.starwars.service.OnlineDataService;
import com.example.starwars.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

    private  OnlineDataService onlineDataService;
    private  OfflineDataService offlineDataService;
    private  SearchResultModelAssembler assembler;

    private static final Logger logger = Logger.getLogger(SearchServiceImpl.class.getName());

    @Autowired
    public SearchServiceImpl(SearchResultModelAssembler assembler,
                         OfflineDataService offlineDataService,
                         OnlineDataService onlineDataService) {
        this.onlineDataService = onlineDataService;
        this.offlineDataService = offlineDataService;
        this.assembler = assembler;
    }

    public EntityModel<SearchResult> search(String type, String name) {
        SearchResult result = fetchDataWithFallback(type, name);
        if ("films".equalsIgnoreCase(type) && result.getCount()!=0) {
            enrichFilmResult(result, name);
        }

        return assembler.toModel(result);  // Separate responsibility of assembler
    }

    private SearchResult fetchDataWithFallback(String type, String name) {
        /***
         * in case you want backend OfflineService and cache
         * boolean isOffline = (type == null);

        if (isOffline) {
            logger.info("Offline mode is enabled");
            return offlineDataService.fetchData(type, name);
        }

        logger.info("Offline mode is disabled");
        */
        logger.info("Online mode  is enabled");
        SearchResult onlineResult = onlineDataService.fetchData(type, name).getContent();

        if (onlineResult == null )  {
            logger.info("Online fetch returned null, falling back to offline");
            // return offlineDataService.fetchData(type, name);
        }

        logger.info("Online result: " + onlineResult);
        return onlineResult;
    }

    private void enrichFilmResult(SearchResult result, String name) {
        result.setName(name);
        result.setFilms(Collections.singletonList(name));
    }

    @CacheEvict(value = "searchResults", key = "#type + '-' + #name")
    public void updateSearchData(String type, String name, SearchResult updatedData) {
        offlineDataService.updateData(type, name, updatedData);
        logger.info("Cache evicted for: " + type + " - " + name);
    }

    @CacheEvict(value = "searchResults", allEntries = true)
    public void evictAllSearchResultsCache() {
        logger.info("All search results cache cleared.");
    }
}
