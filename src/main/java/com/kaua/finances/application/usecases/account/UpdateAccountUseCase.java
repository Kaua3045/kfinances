package com.kaua.finances.application.usecases.account;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.usecases.account.output.UpdateAccountOutput;

public interface UpdateAccountUseCase {

    Either<DomainException, UpdateAccountOutput> execute(String anId, String aName, String aPassword);
}
