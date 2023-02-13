package com.kaua.finances.application.usecases.account;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.EmailAlreadyExistsException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.application.usecases.account.output.CreateAccountOutput;

import java.util.Objects;

public class DefaultCreateAccountUseCase implements CreateAccountUseCase {

    private final AccountGateway accountGateway;

    public DefaultCreateAccountUseCase(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
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

        return Either.right(CreateAccountOutput.from(this.accountGateway.create(aAccount).getId()));
    }
}
