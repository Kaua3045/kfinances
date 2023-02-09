package com.kaua.finances.domain.account;

public record CreateAccountOutput(String id) {

    public static CreateAccountOutput from(final String id) {
        return new CreateAccountOutput(id);
    }
}
