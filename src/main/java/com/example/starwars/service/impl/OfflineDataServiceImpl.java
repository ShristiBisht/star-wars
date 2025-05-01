package com.example.starwars.service.impl;

import com.example.starwars.model.SearchResult;
import com.example.starwars.repository.InMemoryCache;
import com.example.starwars.service.OfflineDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OfflineDataServiceImpl implements OfflineDataService {

    private static final Logger logger = LoggerFactory.getLogger(OfflineDataServiceImpl.class);

    private final InMemoryCache inMemoryCache;

    @Autowired
    public OfflineDataServiceImpl(InMemoryCache inMemoryCache) {
        this.inMemoryCache = inMemoryCache;
    }

    @Override
    @Cacheable(value = "offlineSearchResults", key = "#type + '-' + #name")
    public SearchResult fetchData(String type, String name) {
        SearchResult result = inMemoryCache.get(type, name);
        if (result != null) {
            logger.info("Cache hit for: {} - {}", type, name);
        } else {
            logger.info("Cache miss for: {} - {}", type, name);
        }
        return result;
    }

    @Override
    public void updateData(String type, String name, SearchResult result) {
        inMemoryCache.update(type, name, result);
        logger.info("Cache updated for: {} - {}", type, name);
    }

    @Override
    @CacheEvict(value = "offlineSearchResults", key = "#type + '-' + #name")
    public void evictSearchResultFromCache(String type, String name) {
        logger.info("Evicting cache for: {} - {}", type, name);
    }
}
