package com.kaua.finances.domain.account;

public record CreateAccountOutput(String id) {

    public static CreateAccountOutput from(final Account aAccount) {
        return new CreateAccountOutput(aAccount.getId());
    }
}
