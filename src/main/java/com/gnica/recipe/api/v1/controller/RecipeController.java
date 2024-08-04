package com.gnica.recipe.api.v1.controller;

import com.gnica.recipe.RecipesApi;
import com.gnica.recipe.model.InputRecipeDto;
import com.gnica.recipe.model.RecipeDto;
import com.gnica.recipe.model.RecipeType;
import com.gnica.recipe.search.SearchRequest;
import com.gnica.recipe.service.RecipeService;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RecipeController implements RecipesApi {

    private final RecipeService recipeService;

    @Override
    public ResponseEntity<List<RecipeDto>> allRecipes(
            RecipeType type,
            Integer servings,
            String instructions,
            Set<String> ingredients,
            Set<String> ingredientsExclude) {

        var request = SearchRequest.builder()
                .recipeType(type)
                .servings(servings)
                .instructions(instructions)
                .ingredients(removeEmpty(ingredients))
                .ingredientsToExclude(removeEmpty(ingredientsExclude))
                .build();

        var recipes = recipeService.filterRecipes(request);

        return ResponseEntity.ok(recipes);
    }

    @Override
    public ResponseEntity<RecipeDto> getRecipe(UUID id) {

        var recipe = recipeService.findById(id);
        return ResponseEntity.ok(recipe);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> saveRecipes(List<InputRecipeDto> inputRecipeDto) {
        var recipeDto = recipeService.saveRecipes(inputRecipeDto);

        return ResponseEntity.ok(recipeDto);
    }

    @Override
    public ResponseEntity<RecipeDto> updateRecipe(UUID id, InputRecipeDto inputRecipeDto) {
        var recipeDto = recipeService.updateRecipe(id, inputRecipeDto);

        return ResponseEntity.ok(recipeDto);
    }

    @Override
    public ResponseEntity<Void> deleteRecipe(UUID id) {

        recipeService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    private Set<String> removeEmpty(Set<String> ingredients) {
        if (CollectionUtils.isEmpty(ingredients)) {
            return ingredients;
        }

        return ingredients.stream()
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
