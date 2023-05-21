package com.gnica.recipe.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class RecipeDto {

    private UUID id;
    private String description;
    private String recipeType;
    private String instructions;
    private Integer servings;
    private List<IngredientDto> ingredients;
}
