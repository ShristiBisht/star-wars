package com.example.starwars.star_wars.service;


import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.example.starwars.star_wars.model.SearchResult;



@Service
public class SearchService {

    @Autowired
    private OnlineDataService onlineDataService;
    
    @Autowired
    private OfflineDataService offlineDataService;
    Logger logger = Logger.getLogger("SearchService.java");

    public SearchResult search(String type, String name, boolean offlineMode) {
        SearchResult result =  new SearchResult();
        
        if (offlineMode) {
            logger.info("Offline mode is enabled now");
            // In offline mode, use the offline data service (cache)
            result = offlineDataService.fetchData(type, name);
        } 
        else{
            result=onlineDataService.fetchData(type, name);
            Logger logger = Logger.getLogger("SearchResult.class");

            if (result == null) {
                result = offlineDataService.fetchData(type, name);
                logger.info("Final result is "+result);                
            }
        }

        return result;
    }
    // Evict cache when data is updated or deleted
    @CacheEvict(value = "searchResults", key = "#type + '-' + #name")
    public void updateSearchData(String type, String name, SearchResult updatedData) {
    
        // Optionally, update offline data as well
        offlineDataService.updateData(type, name, updatedData);

        logger.info("Cache evicted for: " + type + " - " + name);
    }

    // Evict all entries in the search cache (for example, when the cache needs to be completely cleared)
    @CacheEvict(value = "searchResults", allEntries = true)
    public void evictAllSearchResultsCache() {
        logger.info("All search results cache cleared.");
    }
}
