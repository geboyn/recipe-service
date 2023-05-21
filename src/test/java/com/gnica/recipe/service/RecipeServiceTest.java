package com.gnica.recipe.service;

import com.gnica.recipe.exception.RecipeNotFoundException;
import com.gnica.recipe.mapper.RecipeMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static com.gnica.recipe.helper.RecipeDataFactory.createTestInputRecipeDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeMapper mapper;

    @DisplayName("Recipe is successfully saved in the database")
    @Test
    void testSaveRecipe() {
        // given an InputRecipeDto
        var inputRecipeDto = createTestInputRecipeDto();

        // when it's saved to the database
        var recipeDtos = recipeService.saveRecipes(List.of(inputRecipeDto));

        // then the object can be retrieved from the service
        var result = recipeService.findById(recipeDtos.get(0).getId());

        assertEquals(result, recipeDtos.get(0));
    }

    @DisplayName("Recipe is successfully saved in the database")
    @Test
    void testDeleteRecipe() {
        // given a recipe

        var inputRecipeDto = createTestInputRecipeDto();
        var recipeDtos = recipeService.saveRecipes(List.of(inputRecipeDto));
        final var id = recipeDtos.get(0).getId();

        // which exists in the database
        var result = recipeService.findById(id);

        assertEquals(result, recipeDtos.get(0));

        // when is deleted
        recipeService.deleteById(id);

        // then the recipe is no longer found
        assertThrows(RecipeNotFoundException.class, () -> recipeService.findById(id));
    }

    @DisplayName("Non existing recipe throws NotFoundException")
    @Test
    void testFindRecipeThrowsException() {
        final var id = UUID.randomUUID();
        assertThrows(RecipeNotFoundException.class, () -> recipeService.findById(id));
    }
}
