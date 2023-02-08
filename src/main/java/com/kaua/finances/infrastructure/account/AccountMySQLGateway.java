package com.kaua.finances.infrastructure.account;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.infrastructure.account.persistence.AccountJpaFactory;
import com.kaua.finances.infrastructure.account.persistence.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class AccountMySQLGateway implements AccountGateway {

    private final AccountRepository accountRepository;

    public AccountMySQLGateway(final AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository);
    }

    @Override
    public Account create(Account aAccount) {
        final var actualEntity = accountRepository.save(AccountJpaFactory.from(aAccount));
        return AccountJpaFactory.toDomain(actualEntity);
    }

    @Override
    public void deleteById(String anId) {

    }

    @Override
    public Optional<Account> findById(String anId) {
        return accountRepository.findById(anId)
                .map(AccountJpaFactory::toDomain);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(AccountJpaFactory::toDomain);
    }

    @Override
    public Account update(Account aAccount) {
        final var actualEntity = accountRepository.save(AccountJpaFactory.from(aAccount));
        return AccountJpaFactory.toDomain(actualEntity);
    }
}
