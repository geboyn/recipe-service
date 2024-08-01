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
import com.gnica.recipe.dto.RecipeDto;
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
    void shouldSaveRecipe() {
        // given an InputRecipeDto
        var inputRecipeDto = createTestInputRecipeDto();

        // when it's saved to the database
        var recipeDtos = recipeService.saveRecipes(List.of(inputRecipeDto));

        // then the object can be retrieved from the service
        var result = recipeService.findById(recipeDtos.get(0).getId());

        assertEquals(result, recipeDtos.get(0));
    }

    @DisplayName("Recipe deleted from database")
    @Test
    void shouldDeleteRecipe() {
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

    @DisplayName("Recipe is updated saved in the database")
    @Test
    void shouldUpdateRecipe() {
        // given
        var inputRecipeDto = createTestInputRecipeDto();
        final var updatedName = "Updated name";

        // a recipe
        var recipeDtos = recipeService.saveRecipes(List.of(inputRecipeDto));

        // when the recipe is updated
        inputRecipeDto.setName(updatedName);

        RecipeDto recipeDto = recipeService.updateRecipe(recipeDtos.get(0).getId(), inputRecipeDto);

        // then the recipe has the updated field
        assertEquals(updatedName, recipeDto.getName());
    }

    @DisplayName("Recipe update fails not found exception")
    @Test
    void shouldNotUpdateRecipeNotFound() {
        // given a recipe update
        var inputRecipeDto = createTestInputRecipeDto();
        final var updatedName = "Updated name";
        final var nonExistingId = UUID.randomUUID();

        // when the recipe is updated
        inputRecipeDto.setName(updatedName);

        // then exception is thrown
        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(nonExistingId, inputRecipeDto));
    }

    @DisplayName("Non existing recipe throws NotFoundException")
    @Test
    void testFindRecipeThrowsException() {
        final var id = UUID.randomUUID();
        assertThrows(RecipeNotFoundException.class, () -> recipeService.findById(id));
    }

    @DisplayName("Vegetarian recipes are filtered successfully")
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
        assertEquals(RecipeType.VEGETARIAN.name(), recipes.get(0).getType());
    }

    @DisplayName("Should filter recipe by multiple fields")
    @Test
    void shouldFilterRecipeByMultipleFields() throws IOException {
        // given 3 recipes
        saveRecipes();

        // when filtering by servings and ingredients with and without
        var searchRequest = SearchRequest.builder()
                .servings(4)
                .ingredients(Set.of("potatos"))
                .ingredientsToExclude(Set.of("pork"))
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

    @DisplayName("Should filter recipe by multiple fields")
    @Test
    void shouldFilterRecipeByMultipleFields2() {
        // given some various recipes
        var fries = new IngredientDto("fries", "300 grams");
        var salad = new IngredientDto("lettuce", "300 grams");
        var bacon = new IngredientDto("bacon", "150 grams");
        var cheese = new IngredientDto("cheese", "30 grams");
        var butter = new IngredientDto("butter", "20 grams");

        var recipe1 =
                createTestInputRecipeDto("Basic recipe 1", RecipeType.STANDARD, Set.of(fries, bacon, cheese, butter));
        var recipe2 = createTestInputRecipeDto("Basic recipe 1", RecipeType.STANDARD, Set.of(bacon, cheese, butter));
        var recipe3 = createTestInputRecipeDto("Basic recipe 2", RecipeType.STANDARD, Set.of(fries, bacon, cheese));
        var recipe4 = createTestInputRecipeDto("Basic recipe 3", RecipeType.STANDARD, Set.of(fries, cheese));
        var recipe5 = createTestInputRecipeDto("Vegetarian recipe 1", RecipeType.VEGETARIAN, Set.of(fries));
        var recipe6 = createTestInputRecipeDto("Vegetarian recipe 2", RecipeType.VEGETARIAN, Set.of(fries, salad));

        recipeService.saveRecipes(List.of(recipe1, recipe2, recipe3, recipe4, recipe5, recipe6));

        // when filtering by servings and ingredients with and without
        // then the correct recipes are returned

        var withoutButter =
                SearchRequest.builder().ingredientsToExclude(Set.of("butter")).build();

        var recipesWithoutButter = recipeService.filterRecipes(withoutButter);
        assertEquals(4, recipesWithoutButter.size());

        var withoutButterAndWithoutCheese = SearchRequest.builder()
                .ingredientsToExclude(Set.of("cheese", "butter"))
                .build();

        var recipesWithoutButterAndCheese = recipeService.filterRecipes(withoutButterAndWithoutCheese);
        assertEquals(2, recipesWithoutButterAndCheese.size());

        var vegetarian =
                SearchRequest.builder().recipeType(RecipeType.VEGETARIAN).build();

        var vegetarianRecipes = recipeService.filterRecipes(vegetarian);
        assertEquals(2, vegetarianRecipes.size());

        var withFriesWithoutCheese = SearchRequest.builder()
                .ingredientsToExclude(Set.of("cheese"))
                .ingredients(Set.of("fries"))
                .build();

        var recipesWithFriesWithoutCheese = recipeService.filterRecipes(withFriesWithoutCheese);
        assertEquals(2, recipesWithFriesWithoutCheese.size());
    }

    private void saveRecipes() throws IOException {
        var request = IOUtils.toString(
                Objects.requireNonNull(this.getClass().getResource("/requests/recipes.json")), StandardCharsets.UTF_8);

        var objectMapper = new ObjectMapper();
        var inputRecipeDtos = objectMapper.readValue(request, new TypeReference<List<InputRecipeDto>>() {});

        recipeService.saveRecipes(inputRecipeDtos);
    }
}
