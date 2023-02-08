package com.kaua.finances.application.exceptions;

import com.kaua.finances.domain.validate.Error;

import java.util.Collections;
import java.util.List;

public class EmailAlreadyExistsException extends NoStackTraceException {

    protected final List<Error> errors;

    protected EmailAlreadyExistsException(final String anMessage, final List<Error> anErrors) {
        super(anMessage);
        this.errors = anErrors;
    }

    public static EmailAlreadyExistsException with() {
        return new EmailAlreadyExistsException("Email already exists", Collections.emptyList());
    }

    public List<Error> getErrors() {
        return errors;
    }
}
