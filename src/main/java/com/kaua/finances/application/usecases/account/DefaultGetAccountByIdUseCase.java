package com.kaua.finances.application.usecases.account;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.account.AccountOutput;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetAccountByIdUseCase implements GetAccountByIdUseCase {

    private final AccountGateway accountGateway;

    public DefaultGetAccountByIdUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public AccountOutput execute(String id) {
        final var aAccount = this.accountGateway.findById(id)
                .orElseThrow(notFound(id));


        return AccountOutput.from(aAccount);
    }

    private static Supplier<NotFoundException> notFound(final String id) {
        return () -> NotFoundException.with(Account.class, id);
    }
}
