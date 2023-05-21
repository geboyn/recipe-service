package com.gnica.recipe.exception.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the pojo used to return the errors for the http requests
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {

    private String message;
    private String errorCode;
}
