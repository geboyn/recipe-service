package com.gnica.recipe.service;

import static com.gnica.recipe.exception.pojo.Errors.RECIPE_NOT_FOUND;
import static com.gnica.recipe.repository.RecipeRepository.Specifications.doesNotContainIngredients;
import static com.gnica.recipe.repository.RecipeRepository.Specifications.withIngredients;
import static com.gnica.recipe.repository.RecipeRepository.Specifications.withInstructions;
import static com.gnica.recipe.repository.RecipeRepository.Specifications.withRecipeType;
import static com.gnica.recipe.repository.RecipeRepository.Specifications.withServings;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import com.gnica.recipe.entity.Recipe;
import com.gnica.recipe.exception.RecipeNotFoundException;
import com.gnica.recipe.mapper.RecipeMapper;
import com.gnica.recipe.repository.RecipeRepository;
import com.gnica.recipe.search.SearchRequest;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {

    private final RecipeMapper mapper;
    private final RecipeRepository recipeRepository;

    @Transactional
    public List<RecipeDto> saveRecipes(List<InputRecipeDto> inputRecipeDto) {
        var entities = mapper.toEntities(inputRecipeDto);
        var savedEntity = recipeRepository.saveAll(entities);

        return mapper.fromEntities(savedEntity);
    }

    @Transactional
    public RecipeDto findById(UUID id) {
        return recipeRepository
                .findById(id)
                .map(mapper::fromEntity)
                .orElseThrow(() ->
                        new RecipeNotFoundException(RECIPE_NOT_FOUND.getErrorCode(), RECIPE_NOT_FOUND.getMessage()));
    }

    @Transactional
    public List<RecipeDto> filterRecipes(SearchRequest searchRequest) {

        Specification<Recipe> specs = Specification.where(withIngredients(searchRequest.getIngredients()))
                .and(doesNotContainIngredients(searchRequest.getIngredientsExclude()))
                .and(withRecipeType(searchRequest.getRecipeType()))
                .and(withServings(searchRequest.getServings()))
                .and(withInstructions(searchRequest.getInstructions()));

        var recipes = recipeRepository.findAll(specs);

        return mapper.fromEntities(recipes);
    }

    public void deleteById(UUID id) {
        recipeRepository.deleteById(id);
    }
}
