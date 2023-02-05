package com.kaua.finances.infrastructure.account.persistence;

import com.kaua.finances.domain.account.Account;

import java.util.ArrayList;

public class AccountJpaFactory {

    public static AccountJpaEntity from(final Account aAccount) {
        return new AccountJpaEntity(
                aAccount.getId(),
                aAccount.getName(),
                aAccount.getEmail(),
                aAccount.getPassword(),
                aAccount.getCreatedAt(),
                aAccount.getUpdatedAt()
        );
    }

    public static Account toDomain(final AccountJpaEntity aAccountJpa) {
        return Account.with(
                aAccountJpa.getId(),
                aAccountJpa.getName(),
                aAccountJpa.getEmail(),
                aAccountJpa.getPassword(),
                new ArrayList<>(),
                aAccountJpa.getCreatedAt(),
                aAccountJpa.getUpdatedAt()
        );
    }
}
