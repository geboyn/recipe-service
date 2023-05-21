package com.gnica.recipe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RecipeNotFoundException extends ServiceException {

    private final HttpStatus httpStatus;

    public RecipeNotFoundException(String name, String message) {
        super(name, message);
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
