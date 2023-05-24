package com.gnica.recipe.search;

import com.gnica.recipe.dto.RecipeType;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRequest {

    private RecipeType recipeType;
    private Integer servings;
    private String instructions;
    private Set<String> ingredients;
    private Set<String> ingredientsToExclude;
}
