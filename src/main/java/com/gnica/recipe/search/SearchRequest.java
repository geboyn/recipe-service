package com.gnica.recipe.search;

import com.gnica.recipe.dto.RecipeType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SearchRequest {

    private RecipeType recipeType;
    private Integer servings;
    private String instructions;
    private Set<String> ingredients;
    private Set<String> ingredientsExclude;
}