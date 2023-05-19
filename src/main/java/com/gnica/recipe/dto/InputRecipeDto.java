package com.gnica.recipe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class InputRecipeDto {

    public enum RecipeType {
        STANDARD, VEGETARIAN, VEGAN;

        @JsonCreator
        public static RecipeType getValueOrDefault(String value) {
            return Arrays.stream(RecipeType.values()).filter(aEnum -> aEnum.name().equalsIgnoreCase(value)).findFirst().orElse(RecipeType.STANDARD);
        }
    }

    private String description;
    private RecipeType recipeType;
    private String instructions;
    private int numberOfServings;

    private Set<IngredientDto> ingredients;

}
