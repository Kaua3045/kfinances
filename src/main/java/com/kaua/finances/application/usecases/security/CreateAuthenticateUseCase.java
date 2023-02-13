package com.kaua.finances.application.usecases.security;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.authenticate.AuthenticateTokenOutput;

public interface CreateAuthenticateUseCase {

    Either<NotFoundException, AuthenticateTokenOutput> execute(String email, String password);
}
