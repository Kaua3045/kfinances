package com.kaua.finances.application.usecases.account.update;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NoStackTraceException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.application.usecases.account.output.UpdateAccountOutput;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateAccountUseCase implements UpdateAccountUseCase {

    private final AccountGateway accountGateway;
    private final AccountCacheGateway accountCacheGateway;

    public DefaultUpdateAccountUseCase(
            final AccountGateway accountGateway,
            final AccountCacheGateway accountCacheGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountCacheGateway = accountCacheGateway;
    }

    @Override
    public Either<DomainException, UpdateAccountOutput> execute(
            String anId,
            String aName,
            String aPassword
    ) {
        final var aAccount = this.accountGateway.findById(anId)
                .orElseThrow(notFound(anId));

        aAccount.update(aName, aPassword);

        final var aAccountValidate = aAccount.validate();

        if (!aAccountValidate.isEmpty()) {
            return Either.left(DomainException.with(aAccountValidate));
        }

        this.accountCacheGateway.update(aAccount);

        return Either.right(UpdateAccountOutput.from(this.accountGateway.update(aAccount).getId()));
    }

    private static Supplier<NoStackTraceException> notFound(final String id) {
        return () -> NotFoundException.with(Account.class, id);
    }
}
