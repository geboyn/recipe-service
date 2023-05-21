package com.gnica.recipe.dto;

import java.util.Set;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InputRecipeDto {

    private String description;
    private RecipeType recipeType;
    private String instructions;
    private Integer servings;
    private Set<IngredientDto> ingredients;
}
