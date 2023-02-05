package com.kaua.finances.application.exceptions;

import com.kaua.finances.domain.validate.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    protected final List<Error> errors;

    protected DomainException(final List<Error> anErrors) {
        super("Domain Exception");
        this.errors = anErrors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(List.of(anError));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
