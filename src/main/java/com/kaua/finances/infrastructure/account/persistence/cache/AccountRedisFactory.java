package com.kaua.finances.infrastructure.account.persistence.cache;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.utils.InstantUtils;

public class AccountRedisFactory {

    public static AccountRedisEntity from(final Account aAccount) {
        return new AccountRedisEntity(
                aAccount.getId(),
                aAccount.getName(),
                aAccount.getEmail(),
                aAccount.getPassword(),
                aAccount.getCreatedAt(),
                aAccount.getUpdatedAt(),
                InstantUtils.now()
        );
    }

    public static Account toDomain(final AccountRedisEntity aAccountRedis) {
        return Account.with(
                aAccountRedis.getId(),
                aAccountRedis.getName(),
                aAccountRedis.getEmail(),
                aAccountRedis.getPassword(),
                aAccountRedis.getCreatedAt(),
                aAccountRedis.getUpdatedAt()
        );
    }
}
