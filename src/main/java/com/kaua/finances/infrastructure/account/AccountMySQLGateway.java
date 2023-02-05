package com.kaua.finances.infrastructure.account;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.infrastructure.account.persistence.AccountRepository;

import java.util.Objects;
import java.util.Optional;

public class AccountMySQLGateway implements AccountGateway {

    private final AccountRepository accountRepository;

    public AccountMySQLGateway(final AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository);
    }

    @Override
    public Account create(Account aAccount) {
        return null;
    }

    @Override
    public void deleteById(String anId) {

    }

    @Override
    public Optional<Account> findById(String anId) {
        return Optional.empty();
    }

    @Override
    public Account update(Account aAccount) {
        return null;
    }
}
