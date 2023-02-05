package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.infrastructure.utils.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(new ApiError(ex.getMessage(), ex.getErrors()));
    }
}
