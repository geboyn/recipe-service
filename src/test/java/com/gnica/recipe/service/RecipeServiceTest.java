package com.gnica.recipe.service;

import static com.gnica.recipe.helper.RecipeDataFactory.createTestInputRecipeDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnica.recipe.dto.IngredientDto;
import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeType;
import com.gnica.recipe.exception.RecipeNotFoundException;
import com.gnica.recipe.repository.RecipeRepository;
import com.gnica.recipe.search.SearchRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    public void setup() {
        recipeRepository.deleteAll();
    }

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

    @Test
    void shouldFilterVegetarianRecipes() throws IOException {
        // given 3 recipes with one VEGETARIAN
        saveRecipes();

        // when filtering by VEGETARIAN
        var searchRequest =
                SearchRequest.builder().recipeType(RecipeType.VEGETARIAN).build();

        // then the vegetarian recipe is returned
        var recipes = recipeService.filterRecipes(searchRequest);

        assertEquals(1, recipes.size());
        assertEquals(RecipeType.VEGETARIAN.name(), recipes.get(0).getRecipeType());
    }

    @Test
    void shouldFilterRecipeByMultipleFields() throws IOException {
        // given 3 recipes
        saveRecipes();

        // when filtering by servings and ingredients with and without
        var searchRequest = SearchRequest.builder()
                .servings(4)
                .ingredients(Set.of("potatos"))
                .ingredientsExclude(Set.of("pork"))
                .instructions("bake")
                .build();

        // then the correct recipe is returned
        var recipes = recipeService.filterRecipes(searchRequest);

        assertEquals(1, recipes.size());

        // with the correct ingredients
        recipes.forEach(recipeDto -> {
            var ingredients = recipeDto.getIngredients().stream()
                    .map(IngredientDto::getName)
                    .toList();
            assertFalse(ingredients.contains("pork"));
            assertTrue(ingredients.contains("potatos"));
            assertEquals(4, recipeDto.getServings());
        });
    }

    private void saveRecipes() throws IOException {
        var request = IOUtils.toString(
                Objects.requireNonNull(this.getClass().getResource("/requests/recipes.json")), StandardCharsets.UTF_8);

        var objectMapper = new ObjectMapper();
        var inputRecipeDtos = objectMapper.readValue(request, new TypeReference<List<InputRecipeDto>>() {});

        recipeService.saveRecipes(inputRecipeDtos);
    }
}
