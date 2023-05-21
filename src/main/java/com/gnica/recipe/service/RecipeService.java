package com.gnica.recipe.service;

import static com.gnica.recipe.exception.pojo.Errors.RECIPE_NOT_FOUND;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import com.gnica.recipe.entity.Ingredient;
import com.gnica.recipe.entity.Recipe;
import com.gnica.recipe.exception.RecipeNotFoundException;
import com.gnica.recipe.mapper.RecipeMapper;
import com.gnica.recipe.repository.RecipeRepository;
import com.gnica.recipe.search.SearchRequest;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    public List<RecipeDto> findByCriteria(SearchRequest searchRequest) {

        var recipes = filterRecipes(searchRequest).stream()
                .filter(p -> containsIngredient(searchRequest, getIngredientNames(p)))
                .filter(p -> doesNotContainAnyIngredient(searchRequest, getIngredientNames(p)))
                .toList();

        return mapper.fromEntities(recipes);
    }

    private boolean doesNotContainAnyIngredient(SearchRequest searchRequest, Set<String> recipeIngredients) {
        if (CollectionUtils.isEmpty(searchRequest.getIngredientsExclude())) {
            return true;
        }
        return !CollectionUtils.containsAny(recipeIngredients, searchRequest.getIngredientsExclude());
    }

    private boolean containsIngredient(SearchRequest searchRequest, Set<String> recipeIngredients) {
        if (CollectionUtils.isEmpty(searchRequest.getIngredients())) {
            return true;
        } else {
            return recipeIngredients.containsAll(searchRequest.getIngredients());
        }
    }

    private Set<String> getIngredientNames(Recipe recipe) {
        return recipe.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toSet());
    }

    private List<Recipe> filterRecipes(SearchRequest searchRequest) {
        return recipeRepository.findAll((Specification<Recipe>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.getRecipeType() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                        root.get("recipeType"), searchRequest.getRecipeType().toString())));
            }

            if (searchRequest.getServings() != null) {
                predicates.add(
                        criteriaBuilder.and(criteriaBuilder.equal(root.get("servings"), searchRequest.getServings())));
            }

            if (StringUtils.isNotBlank(searchRequest.getInstructions())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("instructions")),
                        '%' + searchRequest.getInstructions().toLowerCase() + '%')));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        });
    }

    public void deleteById(UUID id) {
        recipeRepository.deleteById(id);
    }
}
