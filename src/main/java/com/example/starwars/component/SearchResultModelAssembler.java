package com.example.starwars.component;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.starwars.controller.SearchController;
import com.example.starwars.model.SearchResult;

@Component
public class SearchResultModelAssembler {


    public EntityModel<SearchResult> toModel(SearchResult result) {
        return EntityModel.of(result,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SearchController.class)
                .search(result.getType(), result.getName())).withSelfRel());
    }
}
