package com.gnica.recipe.search.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gnica.recipe.dto.RecipeType;
import com.gnica.recipe.exception.InvalidRecipeException;
import com.gnica.recipe.search.SearchParameters;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class SearchRequestParserTest {

    private final SearchRequestParser searchRequestParser = new SearchRequestParser();

    @DisplayName("Test request is parsed successfully")
    @Test
    void shouldParseRequest() {
        var mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setParameter(SearchParameters.RECIPE_TYPE, "standard");
        mockHttpServletRequest.setParameter(SearchParameters.SERVINGS, "3");
        mockHttpServletRequest.setParameter(SearchParameters.INSTRUCTIONS, "bake me");
        mockHttpServletRequest.setParameter(SearchParameters.INGREDIENTS, "milk");
        mockHttpServletRequest.setParameter(SearchParameters.INGREDIENTS_EXCLUDE, "cocoa");

        var searchRequest = searchRequestParser.parseRequest(mockHttpServletRequest);

        assertEquals(RecipeType.getValue("standard"), searchRequest.getRecipeType());
        assertEquals(3, searchRequest.getServings());
        assertEquals("bake me", searchRequest.getInstructions());
        assertEquals(Set.of("milk"), searchRequest.getIngredients());
        assertEquals(Set.of("cocoa"), searchRequest.getIngredientsExclude());
    }

    @DisplayName("Test parsing fails due to invalid recipe type")
    @Test
    void shouldThrowExceptionOnWrongRecipeType() {
        var mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setParameter(SearchParameters.RECIPE_TYPE, "oh-oh");

        assertThrows(InvalidRecipeException.class, () -> searchRequestParser.parseRequest(mockHttpServletRequest));
    }
}
