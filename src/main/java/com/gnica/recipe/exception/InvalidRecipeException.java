package com.gnica.recipe.exception;

import lombok.Getter;

@Getter
public class InvalidRecipeException extends ServiceException {

    public InvalidRecipeException(String name, String message) {
        super(name, message);
    }
}
