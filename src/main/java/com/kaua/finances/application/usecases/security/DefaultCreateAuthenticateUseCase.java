package com.kaua.finances.application.usecases.security;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.authenticate.AuthenticateTokenOutput;
import com.kaua.finances.domain.authenticate.SecurityGateway;

public class DefaultCreateAuthenticateUseCase implements CreateAuthenticateUseCase {

    private final AccountGateway accountGateway;
    private final SecurityGateway securityGateway;

    public DefaultCreateAuthenticateUseCase(AccountGateway accountGateway, SecurityGateway securityGateway) {
        this.accountGateway = accountGateway;
        this.securityGateway = securityGateway;
    }

    @Override
    public Either<NotFoundException, AuthenticateTokenOutput> execute(String email, String password) {
        final var aAccount = this.accountGateway.findByEmail(email);

        if (aAccount.isEmpty()) {
            return Either.left(NotFoundException.with(Account.class, email));
        }

        final var aToken = this.securityGateway.generateToken(aAccount.get().getId());

        return Either.right(new AuthenticateTokenOutput(aToken));
    }
}
