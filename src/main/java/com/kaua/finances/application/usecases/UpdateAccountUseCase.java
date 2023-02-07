package com.kaua.finances.application.usecases;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.domain.account.UpdateAccountOutput;

import java.util.List;

public interface UpdateAccountUseCase {

    Either<DomainException, UpdateAccountOutput> execute(String anId, String aName, String aPassword, List<String> bills);
}
