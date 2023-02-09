package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.domain.bills.CreateBillOutput;

public interface CreateBillUseCase {

    Either<DomainException, CreateBillOutput> execute(
            String accountId,
            String title,
            String description,
            boolean pending
    );
}
