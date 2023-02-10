package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.domain.bills.CreateBillOutput;

import java.util.Objects;

public class DefaultCreateBillUseCase implements CreateBillUseCase {

    private final BillGateway billGateway;
    private final AccountGateway accountGateway;

    public DefaultCreateBillUseCase(final BillGateway billGateway, final AccountGateway accountGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Either<DomainException, CreateBillOutput> execute(
            final String accountId,
            final String title,
            final String description,
            final boolean pending
    ) {
        final var accountValid = this.accountGateway.findById(accountId)
                .orElseThrow(() -> NotFoundException.with(Account.class, accountId));

        final var aBill = Bill.newBill(accountValid, title, description, pending);
        final var aBillValidate = aBill.validate();

        if (!aBillValidate.isEmpty()) {
            return Either.left(DomainException.with(aBillValidate));
        }

        return Either.right(CreateBillOutput.from(this.billGateway.create(aBill).getId()));
    }
}
