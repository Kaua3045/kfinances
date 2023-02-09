package com.kaua.finances.application.usecases.account;

import com.kaua.finances.domain.account.AccountGateway;

import java.util.Objects;

public class DefaultDeleteAccountByIdUseCase implements DeleteAccountByIdUseCase {

    private final AccountGateway accountGateway;

    public DefaultDeleteAccountByIdUseCase(AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public void execute(String id) {
        this.accountGateway.deleteById(id);
    }
}
