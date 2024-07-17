package com.example.librarymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    private String resourceName;

    public EntityNotFoundException(String resourceName) {
        super(String.format("%s not found", resourceName));
        this.resourceName = resourceName;
    }
}