package com.gnica.recipe.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class InputRecipeDto {

    private String name;
    private RecipeType type;
    private String instructions;
    private Integer servings;
    private Set<IngredientDto> ingredients;
}
