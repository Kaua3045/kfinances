package com.kaua.finances.application.usecases;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NoStackTraceException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.account.UpdateAccountOutput;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateAccountUseCase implements UpdateAccountUseCase {

    private final AccountGateway accountGateway;

    public DefaultUpdateAccountUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Either<DomainException, UpdateAccountOutput> execute(
            String anId,
            String aName,
            String aPassword,
            List<String> aBills
    ) {
        final var aAccount = this.accountGateway.findById(anId)
                .orElseThrow(notFound(anId));

        // TODO: implementar essa parte dos bills, o update vai precisar verificar se existe todos os ids
        aAccount.update(aName, aPassword, Collections.emptyList());
        final var aAccountValidate = aAccount.validate();

        if (!aAccountValidate.isEmpty()) {
            return Either.left(DomainException.with(aAccountValidate));
        }

        return Either.right(UpdateAccountOutput.from(this.accountGateway.update(aAccount)));
    }

    private static Supplier<NoStackTraceException> notFound(final String id) {
        return () -> NotFoundException.with(Account.class, id);
    }
}
