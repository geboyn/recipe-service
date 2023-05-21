package com.gnica.recipe.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gnica.recipe.exception.InvalidRecipeException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InputRecipeDtoTest {

    private static Stream<Arguments> provideInputs() {
        return Stream.of(
                Arguments.of("Standard", RecipeType.STANDARD),
                Arguments.of("VegETARian", RecipeType.VEGETARIAN),
                Arguments.of("vegan", RecipeType.VEGAN));
    }

    @DisplayName("Invalid recipe type throws exception")
    @Test
    void shouldThrowInvalidRecipeException() {
        assertThrows(InvalidRecipeException.class, () -> RecipeType.getValue("test"));
    }

    @ParameterizedTest
    @MethodSource(value = "provideInputs")
    void shouldMapRecipeType(String input, RecipeType recipeType) {
        assertEquals(RecipeType.getValue(input), recipeType);
    }
}
