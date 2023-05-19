package com.gnica.recipe.service;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;

import java.util.List;
import java.util.UUID;

public interface RecipeService {

    RecipeDto saveRecipe(InputRecipeDto inputRecipeDto);

    RecipeDto findById(UUID id);

    List<RecipeDto> findAll();

    void deleteById(UUID id);
}
