package com.kaua.finances.application.usecases.account.output;

public record CreateAccountOutput(String id) {

    public static CreateAccountOutput from(final String id) {
        return new CreateAccountOutput(id);
    }
}
