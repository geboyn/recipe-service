package com.gnica.recipe.api.v1.controller;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import com.gnica.recipe.search.parser.SearchRequestParser;
import com.gnica.recipe.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RecipeController implements RecipeApi {

    private final RecipeService recipeService;

    @Override
    public ResponseEntity<List<RecipeDto>> allRecipes(HttpServletRequest request) {

        var searchRequestParser = new SearchRequestParser();
        var searchRequest = searchRequestParser.parseRequest(request);
        var recipes = recipeService.filterRecipes(searchRequest);
        return ResponseEntity.ok(recipes);
    }

    @Override
    public ResponseEntity<RecipeDto> getRecipe(UUID id) {

        var recipe = recipeService.findById(id);
        return ResponseEntity.ok(recipe);
    }

    @Override
    public ResponseEntity<List<RecipeDto>> saveRecipes(@RequestBody List<InputRecipeDto> inputRecipeDto) {

        var recipeDto = recipeService.saveRecipes(inputRecipeDto);
        return ResponseEntity.ok(recipeDto);
    }

    @Override
    public ResponseEntity<Void> deleteRecipe(@PathVariable UUID id) {

        recipeService.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}
