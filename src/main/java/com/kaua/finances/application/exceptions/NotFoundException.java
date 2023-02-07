package com.kaua.finances.application.exceptions;

import com.kaua.finances.domain.validate.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends NoStackTraceException {

    protected final List<Error> errors;

    protected NotFoundException(final String anMessage, final List<Error> anErrors) {
        super(anMessage);
        this.errors = anErrors;
    }

    public static NotFoundException with(final Class<?> clazz, final String id) {
        final var anError = "%s with ID %s was not found".formatted(
                clazz.getSimpleName(),
                id
        );

        return new NotFoundException(anError, Collections.emptyList());
    }

    public List<Error> getErrors() {
        return errors;
    }
}
