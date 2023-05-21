package com.gnica.recipe.search;

/**
 * Contains the parameters that can be used for filtering the recipes
 */
public class SearchParameters {

    private SearchParameters() {}

    public static final String RECIPE_TYPE = "recipeType";
    public static final String SERVINGS = "servings";
    public static final String INSTRUCTIONS = "instructions";
    public static final String INGREDIENTS = "ingredients";
    public static final String INGREDIENTS_EXCLUDE = "ingredientsExclude";
}
