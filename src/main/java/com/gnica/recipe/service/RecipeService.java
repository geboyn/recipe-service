package com.gnica.recipe.service;

import com.gnica.recipe.dto.InputRecipeDto;
import com.gnica.recipe.dto.RecipeDto;
import com.gnica.recipe.entity.Ingredient;
import com.gnica.recipe.entity.Recipe;
import com.gnica.recipe.exception.RecipeNotFoundException;
import com.gnica.recipe.mapper.RecipeMapper;
import com.gnica.recipe.repository.RecipeRepository;
import com.gnica.recipe.search.SearchRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.gnica.recipe.exception.pojo.Errors.RECIPE_NOT_FOUND;

@Service
@RequiredArgsConstructor
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
        return recipeRepository.findById(id)
                .map(mapper::fromEntity).orElseThrow(() -> new RecipeNotFoundException(RECIPE_NOT_FOUND.getErrorCode(), RECIPE_NOT_FOUND.getMessage()));
    }

    @Transactional
    public List<RecipeDto> findAll() {

        List<Recipe> all = recipeRepository.findAll();
        return mapper.fromEntities(all);
    }

    public List<RecipeDto> findByCriteria(SearchRequest searchRequest) {


        List<Recipe> all = gabrielFinds(searchRequest)
                .stream()
                .filter(p -> containsIngredient(searchRequest, p))
                .filter(p -> doesNotContainAnyIngredient(searchRequest, p))
                .toList();

        return mapper.fromEntities(all);
    }

    private boolean doesNotContainAnyIngredient(SearchRequest searchRequest, Recipe recipe) {
        if (CollectionUtils.isEmpty(searchRequest.getIngredientsExclude())) {
            return true;
        }

        return !CollectionUtils.containsAny(getIngredients(recipe), searchRequest.getIngredientsExclude());
    }

    private boolean containsIngredient(SearchRequest searchRequest, Recipe recipe) {
        if (CollectionUtils.isEmpty(searchRequest.getIngredients())) {
            return true;
        } else {
            return getIngredients(recipe).containsAll(searchRequest.getIngredients());
        }

    }

    private Set<String> getIngredients(Recipe recipe) {
        Set<String> ingredients = new HashSet<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients.add(ingredient.getName());

        }
        return ingredients;
    }

    public List<Recipe> gabrielFinds(SearchRequest searchRequest) {
        return recipeRepository.findAll(new Specification<Recipe>() {
            @Override
            public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                List<Predicate> predicates = new ArrayList<>();

                if (searchRequest.getRecipeType() != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("recipeType"), searchRequest.getRecipeType().toString())));
                }

                if (searchRequest.getServings() != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("servings"), searchRequest.getServings())));
                }

                /*CriteriaQuery<Recipe> cq = criteriaBuilder.createQuery(Recipe.class);
                Root<Recipe> recipeRoot = cq.from(Recipe.class);

                cq.select(recipeRoot);
                Join<Recipe, Ingredient> ingredientJoin = root.join("ingredients");
                cq.multiselect(root, ingredientJoin);
                cq.where(criteriaBuilder.in(root.get()))*/




/*
            if (searchRequest.getIngredients() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.isMember(searchRequest.getIngredients() , hnJoin.get("name"))));
            }*/

      /*       if (searchRequest.getIngredients() != null ) {

                        CriteriaQuery<Recipe> criteriaQuery =
                                criteriaBuilder.createQuery(Recipe.class);
                            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("ingredients"));

                            for(String ingredient : searchRequest.getIngredients()) {
                                inClause.value(ingredient);
                            }
                 CriteriaQuery<Recipe> where = criteriaQuery.select(root).where(inClause);
             }


                predicates.add(criteriaBuilder.and(ingredientPredicates.toArray(new Predicate[]{})));
            }
*/

          /*  if(StringUtils.isNotBlank(searchRequest.getInstructions())) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("instructions"), '%' +  searchRequest.getInstructions() + '%')));
            }*/

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        });
    }

    public void deleteById(UUID id) {
        recipeRepository.deleteById(id);
    }
}
