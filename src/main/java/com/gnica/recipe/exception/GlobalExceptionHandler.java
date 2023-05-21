package com.gnica.recipe.exception;

import com.gnica.recipe.exception.pojo.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({RecipeNotFoundException.class})
    public ResponseEntity<ApiError> handleRecipeNotFoundException(RecipeNotFoundException ex, WebRequest webRequest) {
        var apiError = new ApiError(ex.getMessage(), ex.getName());
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }

    @ExceptionHandler({InvalidRecipeException.class})
    public ResponseEntity<ApiError> handleInvalidRecipeException(InvalidRecipeException ex, WebRequest webRequest) {
        var apiError = new ApiError(ex.getMessage(), ex.getName());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
