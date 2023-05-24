package com.gnica.recipe.helper;

import com.gnica.recipe.dto.IngredientDto;
import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeType;
import java.util.Set;

public class RecipeDataFactory {

    public static InputRecipeDto createTestInputRecipeDto() {
        var inputRecipeDto = new InputRecipeDto();
        inputRecipeDto.setType(RecipeType.VEGETARIAN);
        inputRecipeDto.setName("French fries");
        inputRecipeDto.setInstructions("Just do it!");
        inputRecipeDto.setServings(3);

        var ingredientDto = new IngredientDto();
        ingredientDto.setName("fries");
        ingredientDto.setQuantity("500 grams");
        inputRecipeDto.setIngredients(Set.of(ingredientDto));

        return inputRecipeDto;
    }

    public static InputRecipeDto createTestInputRecipeDto(
            String name, RecipeType recipeType, Set<IngredientDto> ingredients) {

        return InputRecipeDto.builder()
                .name(name)
                .type(recipeType)
                .instructions("Just do it!")
                .servings(3)
                .ingredients(ingredients)
                .build();
    }
}
