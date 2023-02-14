package com.kaua.finances.application.usecases.account.create;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.EmailAlreadyExistsException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.application.usecases.account.output.CreateAccountOutput;
import com.kaua.finances.domain.account.AccountRedisGateway;

import java.util.Objects;

public class DefaultCreateAccountUseCase implements CreateAccountUseCase {

    private final AccountGateway accountGateway;
    private final AccountRedisGateway accountRedisGateway;

    public DefaultCreateAccountUseCase(final AccountGateway accountGateway, AccountRedisGateway accountRedisGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountRedisGateway = Objects.requireNonNull(accountRedisGateway);
    }

    @Override
    public Either<DomainException, CreateAccountOutput> execute(String aName, String aEmail, String aPassword) {
        final var aAccountExists = this.accountGateway.findByEmail(aEmail);

        if (aAccountExists.isPresent()) {
            return Either.left(DomainException.with(EmailAlreadyExistsException.with()));
        }

        final var aAccount = Account.newAccount(aName, aEmail, aPassword);
        final var aAccountValidated = aAccount.validate();

        if (!aAccountValidated.isEmpty()) {
            return Either.left(DomainException.with(aAccountValidated));
        }

        this.accountRedisGateway.create(aAccount);

        return Either.right(CreateAccountOutput.from(this.accountGateway.create(aAccount).getId()));
    }
}
