package com.gnica.recipe.service;

import com.gnica.recipe.exception.RecipeNotFoundException;
import com.gnica.recipe.mapper.RecipeMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static com.gnica.recipe.helper.RecipeDataFactory.createTestInputRecipeDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RecipeServiceImplTest {

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
        var recipeDto = recipeService.saveRecipe(inputRecipeDto);

        // then the object can be retrieved from the service
        var result = recipeService.findById(recipeDto.getId());

        assertEquals(result, recipeDto);
    }

    @DisplayName("Recipe is successfully saved in the database")
    @Test
    void testDeleteRecipe() {
        // given a recipe
        var inputRecipeDto = createTestInputRecipeDto();
        var recipeDto = recipeService.saveRecipe(inputRecipeDto);
        final var id = recipeDto.getId();

        // which exists in the database
        var result = recipeService.findById(id);

        assertEquals(result, recipeDto);

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