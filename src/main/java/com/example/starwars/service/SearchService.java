package com.example.starwars.service;

import java.util.Collections;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.example.starwars.component.SearchResultModelAssembler;
import com.example.starwars.model.SearchResult;

@Service
public class SearchService {

    private OnlineDataService onlineDataService;
    private OfflineDataService offlineDataService;
    private SearchResultModelAssembler assembler;

    @Autowired
    public SearchService(SearchResultModelAssembler assembler, OfflineDataService offlineDataService,OnlineDataService onlineDataService){
        this.onlineDataService=onlineDataService;
        this.offlineDataService=offlineDataService;
        this.assembler=assembler;
    }


    Logger logger = Logger.getLogger("SearchService");

    public EntityModel<SearchResult> search(String type, String name, boolean offlineMode) {
        SearchResult result;

        if (offlineMode) {
            logger.info("Offline mode is enabled");
            result = offlineDataService.fetchData(type, name);
        } else {
            result = onlineDataService.fetchData(type, name).getContent();
            if (result == null) {
                logger.info("Online fetch returned null, falling back to offline");
                result = offlineDataService.fetchData(type, name);
            }
        }

        if ("films".equalsIgnoreCase(type)) {

        result.setName(name);
        result.setFilms(Collections.singletonList(name));
        }
        result.setOfflineMode(offlineMode);

        return assembler.toModel(result);  // wrap with HATEOAS EntityModel
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
