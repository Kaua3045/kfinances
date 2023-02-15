package com.kaua.finances.infrastructure.account;

import com.kaua.finances.application.usecases.account.output.AccountOutput;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.infrastructure.account.persistence.cache.AccountCacheRepository;
import com.kaua.finances.infrastructure.account.persistence.cache.AccountRedisFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountRedisGateway implements AccountCacheGateway {

    private final AccountCacheRepository accountCacheRepository;

    public AccountRedisGateway(AccountCacheRepository accountCacheRepository) {
        this.accountCacheRepository = accountCacheRepository;
    }

    @Override
    public AccountOutput create(Account aAccount) {
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
    public Optional<AccountOutput> findById(String anId) {
        return this.accountCacheRepository.findById(anId)
                .map((account) -> AccountOutput.from(
                        account.getId(),
                        account.getName(),
                        account.getEmail()
                ));
    }

    @Override
    public AccountOutput update(Account aAccount) {
        final var accountRedis = this.accountCacheRepository.save(AccountRedisFactory.from(aAccount));
        return AccountRedisFactory.toDomain(accountRedis);
    }
}
