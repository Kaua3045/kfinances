package com.kaua.finances.application.usecases.account;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.domain.account.CreateAccountOutput;

public interface CreateAccountUseCase {

    Either<DomainException, CreateAccountOutput> execute(String aName, String aEmail, String aPassword);
}
