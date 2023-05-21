package com.gnica.recipe.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final String name;

    public ServiceException(String name, String message) {
        super(message);
        this.name = name;
    }
}
