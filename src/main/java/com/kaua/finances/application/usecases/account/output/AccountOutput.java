package com.kaua.finances.application.usecases.account.output;

import com.kaua.finances.domain.account.Account;

public record AccountOutput(
        String id,
        String name,
        String email
) {

    public static AccountOutput from(final Account aAccount) {
        return new AccountOutput(
                aAccount.getId(),
                aAccount.getName(),
                aAccount.getEmail()
        );
    }

    public static AccountOutput from(final String id, final String name, final String email) {
        return new AccountOutput(
                id,
                name,
                email
        );
    }
}
