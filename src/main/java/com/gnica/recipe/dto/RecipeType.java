package com.gnica.recipe.dto;

import static com.gnica.recipe.exception.pojo.Errors.INVALID_RECIPE_TYPE;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gnica.recipe.exception.InvalidRecipeException;
import java.util.Arrays;

public enum RecipeType {
    STANDARD,
    VEGETARIAN,
    VEGAN;

    @JsonCreator
    public static RecipeType getValue(String value) {
        return Arrays.stream(RecipeType.values())
                .filter(aEnum -> aEnum.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new InvalidRecipeException(
                        INVALID_RECIPE_TYPE.getErrorCode(), INVALID_RECIPE_TYPE.getMessage()));
    }
}
