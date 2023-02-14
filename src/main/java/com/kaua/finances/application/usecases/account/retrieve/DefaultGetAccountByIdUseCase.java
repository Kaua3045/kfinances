package com.kaua.finances.application.usecases.account.retrieve;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.application.usecases.account.output.AccountOutput;
import com.kaua.finances.domain.account.AccountRedisGateway;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetAccountByIdUseCase implements GetAccountByIdUseCase {

    private final AccountGateway accountGateway;
    private final AccountRedisGateway accountRedisGateway;

    public DefaultGetAccountByIdUseCase(final AccountGateway accountGateway, final AccountRedisGateway accountRedisGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountRedisGateway = Objects.requireNonNull(accountRedisGateway);
    }

    @Override
    public AccountOutput execute(String id) {
        final var aAccount = this.accountRedisGateway.findById(id)
                .orElseGet(() -> this.accountGateway.findById(id).orElseThrow(notFound(id)));

        return AccountOutput.from(aAccount);
    }

    private static Supplier<NotFoundException> notFound(final String id) {
        return () -> NotFoundException.with(Account.class, id);
    }
}
