package com.kaua.finances.application.usecases.account.create;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.usecases.account.output.CreateAccountOutput;

public interface CreateAccountUseCase {

    Either<DomainException, CreateAccountOutput> execute(String aName, String aEmail, String aPassword);
}
