package com.gnica.recipe.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RecipeDto {

    private UUID id;
    private String description;
    private String recipeType;
    private String instructions;
    private int numberOfServings;
    private List<IngredientDto> ingredients;
}
