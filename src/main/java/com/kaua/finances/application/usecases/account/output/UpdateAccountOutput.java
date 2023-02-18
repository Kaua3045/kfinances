package com.kaua.finances.application.usecases.account.output;

public record UpdateAccountOutput(String id) {

    public static UpdateAccountOutput from(final String id) {
        return new UpdateAccountOutput(id);
    }
}
