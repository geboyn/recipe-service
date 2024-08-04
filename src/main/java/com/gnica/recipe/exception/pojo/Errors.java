package com.gnica.recipe.exception.pojo;

import com.gnica.recipe.model.RecipeType;
import java.util.Arrays;
import lombok.Getter;

/**
 * The errors returned by the api
 */
@Getter
public enum Errors {
    RECIPE_NOT_FOUND("RS_001", "Could not find recipe"),
    INVALID_RECIPE_TYPE("RS_002", "Invalid recipe type. Supported types are: " + Arrays.toString(RecipeType.values())),
    INVALID_PARAMETER("RS_003", "Invalid parameter");

    private final String errorCode;
    private final String message;

    Errors(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return getErrorCode();
    }
}
