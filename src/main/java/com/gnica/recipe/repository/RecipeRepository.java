package com.gnica.recipe.repository;

import com.gnica.recipe.entity.Ingredient;
import com.gnica.recipe.entity.Recipe;
import com.gnica.recipe.model.RecipeType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipeRepository extends JpaRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {

    interface Specifications {

        static Specification<Recipe> withIngredients(Set<String> ingredients) {

            if (CollectionUtils.isNotEmpty(ingredients)) {
                return (root, query, criteriaBuilder) -> {
                    Join<Recipe, Ingredient> recipeIngredients = root.join("ingredients");

                    Subquery<UUID> subquery = query.subquery(UUID.class);
                    Root<Ingredient> subRoot = subquery.from(Ingredient.class);
                    Subquery<UUID> where = subquery.select(subRoot.get("id"))
                            .distinct(true)
                            .where(criteriaBuilder.in(subRoot.get("name")).value(ingredients));

                    Predicate id =
                            criteriaBuilder.and(recipeIngredients.get("id").in(where));
                    Expression<Long> countDistinct = criteriaBuilder.countDistinct(recipeIngredients.get("id"));
                    query.groupBy(root.get("id")).having(criteriaBuilder.equal(countDistinct, ingredients.size()));

                    return id;
                };
            }
            return null;
        }

        static Specification<Recipe> doesNotContainIngredients(Set<String> ingredients) {

            if (CollectionUtils.isNotEmpty(ingredients)) {
                return (root, query, criteriaBuilder) -> {
                    Subquery<UUID> subquery = query.subquery(UUID.class);
                    Root<Recipe> subFrom = subquery.from(Recipe.class);
                    Path<String> ingredientName = subFrom.join("ingredients");
                    subquery.select(subFrom.get("id"));
                    subquery.where(
                            criteriaBuilder.in(ingredientName.get("name")).value(ingredients));

                    Path<UUID> id = root.get("id");
                    return criteriaBuilder.not(criteriaBuilder.in(id).value(subquery));
                };
            }
            return null;
        }

        static Specification<Recipe> withRecipeType(RecipeType recipeType) {
            if (recipeType != null) {
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), recipeType.toString()));
            }
            return null;
        }

        static Specification<Recipe> withServings(Integer servings) {
            if (servings == null) {
                return null;
            }
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.and(criteriaBuilder.equal(root.get("servings"), servings));
        }

        static Specification<Recipe> withInstructions(String instructions) {
            if (StringUtils.isBlank(instructions)) {
                return null;
            }
            return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("instructions")), '%' + instructions.toLowerCase() + '%');
        }
    }
}
