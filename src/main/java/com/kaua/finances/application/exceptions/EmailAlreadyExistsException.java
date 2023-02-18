package com.kaua.finances.application.exceptions;

import com.kaua.finances.domain.validate.Error;

import java.util.ArrayList;
import java.util.List;

public class EmailAlreadyExistsException extends NoStackTraceException {

    protected final List<Error> errors;

    protected EmailAlreadyExistsException() {
        super("Email already exists");
        this.errors = new ArrayList<>();
    }

    public static Error with() {
        return new Error(new EmailAlreadyExistsException().getMessage());
    }
}
