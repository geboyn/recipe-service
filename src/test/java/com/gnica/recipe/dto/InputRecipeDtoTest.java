package com.gnica.recipe.dto;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gnica.recipe.model.RecipeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InputRecipeDtoTest {

    @Test
    void shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> RecipeType.fromValue("test"));
    }

    @DisplayName("Invalid recipe type throws exception")
    @ParameterizedTest
    @CsvSource({"test", "StanDard", "Vegetarian", "vegan"})
    void shouldMapRecipeType(String input) {
        assertThrows(IllegalArgumentException.class, () -> RecipeType.fromValue(input));
    }
}
