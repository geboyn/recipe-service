package com.gnica.recipe.exception;

import com.gnica.recipe.exception.pojo.ApiError;
import com.gnica.recipe.exception.pojo.Errors;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest webRequest) {
        var error = String.format(
                "Field %s should be of type %s",
                ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        var apiError = new ApiError(error, Errors.INVALID_PARAMETER.getErrorCode());
        log.error(error);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
