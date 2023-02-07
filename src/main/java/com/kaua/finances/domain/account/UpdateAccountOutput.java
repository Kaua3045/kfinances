package com.kaua.finances.domain.account;

public record UpdateAccountOutput(String id) {

    public static UpdateAccountOutput from(final Account aAccount) {
        return new UpdateAccountOutput(aAccount.getId());
    }
}
