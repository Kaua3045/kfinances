package com.kaua.finances.domain.account;

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
}
