package com.example.starwars.star_wars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.starwars.star_wars.model.SearchResult;
import com.example.starwars.star_wars.repository.InMemoryCache;



/**This class is unused, but in case will have to put in cache if backend is up */

@Service
public class OfflineDataService {

    @Autowired
    private InMemoryCache inMemoryCache;

    @Cacheable(value = "offlineSearchResults", key = "#type + '-' + #name")
    public SearchResult fetchData(String type, String name) {
        SearchResult result = inMemoryCache.get(type, name);
        System.out.println("Cache in here "+result);
        if (result != null) {    
            System.out.println("Cache hit for: " + type + " - " + name);
        } else {
            // Log cache miss
            System.out.println("Cache miss for: " + type + " - " + name);
        }

        return result;
    }

     // Update the cache with new or refreshed data
    public void updateData(String type, String name, SearchResult result) {
        // Put updated data into the cache
        inMemoryCache.update(type, name, result);
        System.out.println("Cache updated for: " + type + " - " + name);
    }

    // Evict cache entry (useful after data update)
    @CacheEvict(value = "offlineSearchResults", key = "#type + '-' + #name")
    public void evictSearchResultFromCache(String type, String name) {
        System.out.println("Evicting cache for: " + type + " - " + name);
    }
}
