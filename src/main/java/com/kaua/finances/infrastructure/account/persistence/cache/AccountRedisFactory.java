package com.kaua.finances.infrastructure.account.persistence.cache;

import com.kaua.finances.application.usecases.account.output.AccountOutput;
import com.kaua.finances.domain.account.Account;

public class AccountRedisFactory {

    public static AccountRedisEntity from(final Account aAccount) {
        return new AccountRedisEntity(
                aAccount.getId(),
                aAccount.getName(),
                aAccount.getEmail()
        );
    }

    public static AccountOutput toDomain(final AccountRedisEntity aAccountRedis) {
        return AccountOutput.from(
                aAccountRedis.getId(),
                aAccountRedis.getName(),
                aAccountRedis.getEmail()
        );
    }
}
