package com.kaua.finances.infrastructure.account;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.domain.utils.InstantUtils;
import com.kaua.finances.infrastructure.account.persistence.cache.AccountRedisFactory;
import com.kaua.finances.infrastructure.account.persistence.cache.AccountCacheRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountRedisGateway implements AccountCacheGateway {

    private final AccountCacheRepository accountCacheRepository;

    public AccountRedisGateway(AccountCacheRepository accountCacheRepository) {
        this.accountCacheRepository = accountCacheRepository;
    }

    @Override
    public Account create(Account aAccount) {
        final var accountRedis = this.accountCacheRepository.save(AccountRedisFactory.from(aAccount));
        return AccountRedisFactory.toDomain(accountRedis);
    }

    @Override
    public void deleteById(String anId) {
        if (this.accountCacheRepository.findById(anId).isPresent()) {
            this.accountCacheRepository.deleteById(anId);
        }
    }

    @Override
    public Optional<Account> findById(String anId) {
        final var accountRedis = this.accountCacheRepository.findById(anId);

        if (accountRedis.isPresent()) {
            accountRedis.get().setLastGetDate(InstantUtils.now());
            this.accountCacheRepository.save(accountRedis.get());

            return accountRedis.map(AccountRedisFactory::toDomain);
        }

        return accountRedis.map(AccountRedisFactory::toDomain);
    }

    @Override
    public Account update(Account aAccount) {
        final var accountRedis = this.accountCacheRepository.save(AccountRedisFactory.from(aAccount));
        return AccountRedisFactory.toDomain(accountRedis);
    }
}
