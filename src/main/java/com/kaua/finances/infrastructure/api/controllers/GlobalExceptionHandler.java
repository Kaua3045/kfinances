package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.EmailAlreadyExistsException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.infrastructure.utils.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(final EmailAlreadyExistsException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(ex.getMessage(), ex.getErrors()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage(), ex.getErrors()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(new ApiError(ex.getMessage(), ex.getErrors()));
    }
}
