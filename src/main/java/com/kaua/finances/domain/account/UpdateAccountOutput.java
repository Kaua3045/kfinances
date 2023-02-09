package com.kaua.finances.domain.account;

public record UpdateAccountOutput(String id) {

    public static UpdateAccountOutput from(final String id) {
        return new UpdateAccountOutput(id);
    }
}
