package com.kaua.finances.domain.account;

import java.util.List;

public record AccountOutput(
        String id,
        String name,
        String email,
        List<String> bills
) {

    public static AccountOutput from(final Account aAccount) {
        return new AccountOutput(
                aAccount.getId(),
                aAccount.getName(),
                aAccount.getEmail(),
                aAccount.getBills()
        );
    }
}
