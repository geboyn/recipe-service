package com.gnica.recipe.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class InputRecipeDto {

    private String description;
    private RecipeType recipeType;
    private String instructions;
    private Integer servings;
    private Set<IngredientDto> ingredients;
}
