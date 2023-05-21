package com.gnica.recipe.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gnica.recipe.dto.IngredientDto;
import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeType;
import com.gnica.recipe.entity.Ingredient;
import com.gnica.recipe.entity.Recipe;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RecipeMapperImpl.class})
class RecipeMapperTest {

    @Autowired
    private RecipeMapper mapper;

    @DisplayName("InputRecipeDto is mapped successfully to RecipeEntity")
    @Test
    void inputRecipeDtoIsMappedSuccessfully() {

        // given an inputRecipeDto
        var inputRecipeDto = createInputRecipeDto();

        // when it's converted to entity
        var entity = mapper.toEntity(inputRecipeDto);

        // then the fields are mapped correctly
        assertEquals(inputRecipeDto.getDescription(), entity.getDescription());
        assertEquals(inputRecipeDto.getRecipeType().name(), entity.getRecipeType());
        assertEquals(inputRecipeDto.getInstructions(), entity.getInstructions());
        assertEquals(inputRecipeDto.getServings(), entity.getServings());

        assertArrayEquals(
                entity.getIngredients().stream()
                        .map(Ingredient::getName)
                        .sorted()
                        .toArray(),
                inputRecipeDto.getIngredients().stream()
                        .map(IngredientDto::getName)
                        .sorted()
                        .toArray());

        assertArrayEquals(
                entity.getIngredients().stream()
                        .map(Ingredient::getQuantity)
                        .sorted()
                        .toArray(),
                inputRecipeDto.getIngredients().stream()
                        .map(IngredientDto::getQuantity)
                        .sorted()
                        .toArray());
    }

    @DisplayName("RecipeEntity is mapped successfully to RecipeDto")
    @Test
    void recipeEntityIsMappedSuccessfully() {

        // given a recipe entity
        var recipe = createRecipeEntity();
        // when it's converted to recipeDto
        var result = mapper.fromEntity(recipe);

        // then the fields are mapped correctly
        assertEquals(recipe.getId(), result.getId());
        assertEquals(recipe.getDescription(), result.getDescription());
    }

    @DisplayName("Recipe entities are mapped successfully to list of recipe dtos")
    @Test
    void recipeEntitiesAreMappedToRecipeDtos() {

        // given a list of recipe entities
        var recipes = List.of(createRecipeEntity());

        // when it's converted to recipeDto
        var result = mapper.fromEntities(recipes);

        // then the fields are mapped correctly
        assertEquals(recipes.get(0).getId(), result.get(0).getId());
        assertEquals(recipes.get(0).getDescription(), result.get(0).getDescription());
    }

    private InputRecipeDto createInputRecipeDto() {
        var inputRecipeDto = new InputRecipeDto();
        inputRecipeDto.setRecipeType(RecipeType.VEGETARIAN);
        inputRecipeDto.setDescription("French fries");
        inputRecipeDto.setInstructions("Just do it!");
        inputRecipeDto.setServings(3);

        var ingredientDto = new IngredientDto();
        ingredientDto.setName("fries");
        ingredientDto.setQuantity("500 grams");
        inputRecipeDto.setIngredients(Set.of(ingredientDto));

        return inputRecipeDto;
    }

    private Recipe createRecipeEntity() {
        var recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        recipe.setDescription("Fries");
        recipe.setRecipeType(RecipeType.VEGETARIAN.toString());

        return recipe;
    }
}
