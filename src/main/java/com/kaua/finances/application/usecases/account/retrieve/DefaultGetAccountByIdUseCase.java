package com.kaua.finances.application.usecases.account.retrieve;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.application.usecases.account.output.AccountOutput;
import com.kaua.finances.domain.account.AccountCacheGateway;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetAccountByIdUseCase implements GetAccountByIdUseCase {

    private final AccountGateway accountGateway;
    private final AccountCacheGateway accountCacheGateway;

    public DefaultGetAccountByIdUseCase(final AccountGateway accountGateway, final AccountCacheGateway accountCacheGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountCacheGateway = Objects.requireNonNull(accountCacheGateway);
    }

    @Override
    public AccountOutput execute(String id) {
        final var aAccount = this.accountCacheGateway.findById(id)
                .orElseGet(() -> AccountOutput.from(this.accountGateway.findById(id)
                                .orElseThrow(notFound(id))
                ));

        return aAccount;
    }

    private static Supplier<NotFoundException> notFound(final String id) {
        return () -> NotFoundException.with(Account.class, id);
    }
}
