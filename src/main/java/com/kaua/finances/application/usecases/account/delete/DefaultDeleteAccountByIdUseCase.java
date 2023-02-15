package com.kaua.finances.application.usecases.account.delete;

import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.domain.account.AccountGateway;

import java.util.Objects;

public class DefaultDeleteAccountByIdUseCase implements DeleteAccountByIdUseCase {

    private final AccountGateway accountGateway;
    private final AccountCacheGateway accountCacheGateway;

    public DefaultDeleteAccountByIdUseCase(AccountGateway accountGateway, AccountCacheGateway accountCacheGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountCacheGateway = accountCacheGateway;
    }

    @Override
    public void execute(String id) {
        this.accountGateway.deleteById(id);
        this.accountCacheGateway.deleteById(id);
    }
}
