package com.gnica.recipe.search.parser;

import com.gnica.recipe.dto.RecipeType;
import com.gnica.recipe.search.SearchRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class SearchRequestParser {

    public SearchRequest parseRequest(HttpServletRequest request) {

        var searchRequest = SearchRequest.builder().build();

        var recipeType = request.getParameter("recipe-type");
        if (recipeType != null) {
            searchRequest.setRecipeType(RecipeType.getValue(recipeType));
        }

        var servingsParam = request.getParameter("servings");
        if (StringUtils.isNotBlank(servingsParam)) {
            int servings = Integer.parseInt(request.getParameter("servings"));
            searchRequest.setServings(servings);
        }

        var instructions = request.getParameter("instructions");
        if (StringUtils.isNotBlank(instructions)) {
            searchRequest.setInstructions(instructions);
        }

        String[] ingredients = request.getParameterValues("ingredients");
        if (ingredients != null) {
            searchRequest.setIngredients(Set.of(ingredients));
        }

        String[] ingredientsExcluded = request.getParameterValues("ingredients-exclude");
        if (ingredientsExcluded != null) {
            searchRequest.setIngredientsExclude(Set.of(ingredientsExcluded));
        }
        return searchRequest;
    }
}
