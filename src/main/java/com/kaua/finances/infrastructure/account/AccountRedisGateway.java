package com.kaua.finances.infrastructure.account;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.infrastructure.account.persistence.cache.AccountRedisFactory;
import com.kaua.finances.infrastructure.account.persistence.cache.AccountRedisRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountRedisGateway implements AccountCacheGateway {

    private final AccountRedisRepository accountRedisRepository;

    public AccountRedisGateway(AccountRedisRepository accountRedisRepository) {
        this.accountRedisRepository = accountRedisRepository;
    }

    @Override
    public Account create(Account aAccount) {
        final var accountRedis = this.accountRedisRepository.save(AccountRedisFactory.from(aAccount));
        return AccountRedisFactory.toDomain(accountRedis);
    }

    @Override
    public void deleteById(String anId) {

    }

    @Override
    public Optional<Account> findById(String anId) {
        return this.accountRedisRepository.findById(anId)
                .map(AccountRedisFactory::toDomain);
    }

    @Override
    public Account update(Account aAccount) {
        return null;
    }
}
