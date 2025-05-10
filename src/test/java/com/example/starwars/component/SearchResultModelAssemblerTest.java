package com.example.starwars.component;

import com.example.starwars.controller.SearchController;
import com.example.starwars.model.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SearchResultModelAssemblerTest {

    private SearchResultModelAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = spy(new SearchResultModelAssembler());
    }

    @Test
    void testToModel_returnsEntityModelWithCorrectLink() {
        // Arrange
        SearchResult mockResult = new SearchResult();
        mockResult.setType("planets");
        mockResult.setName("Tatooine");

        // Act
        EntityModel<SearchResult> model = assembler.toModel(mockResult);

        // Assert content
        assertThat(model.getContent()).isEqualTo(mockResult);

        // Assert self link is correct
        assertThat(model.getLinks()).anySatisfy(link -> {
            assertThat(link.getRel().value()).isEqualTo("self");
            assertThat(link.getHref()).contains("/search");
        });

        // Verify assembler is used
        verify(assembler).toModel(mockResult);
    }
}