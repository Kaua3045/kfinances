package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.domain.bills.UpdateBillOutput;

public interface UpdatePendingBillUseCase {

    Either<DomainException, UpdateBillOutput> execute(String id, boolean pending);
}
