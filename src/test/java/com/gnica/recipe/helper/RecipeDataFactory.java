package com.gnica.recipe.helper;

import com.gnica.recipe.dto.IngredientDto;
import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeType;
import java.util.Set;

public class RecipeDataFactory {

    public static InputRecipeDto createTestInputRecipeDto() {
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
}
