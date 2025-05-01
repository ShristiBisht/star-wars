package com.example.starwars.star_wars.component;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.starwars.star_wars.controller.SearchController;
import com.example.starwars.star_wars.model.SearchResult;

@Component
public class SearchResultModelAssembler {


    public EntityModel<SearchResult> toModel(SearchResult result) {
        return EntityModel.of(result,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SearchController.class)
                .search(result.getType(), result.getName(), result.getOfflineMode())).withSelfRel());
    }
}
