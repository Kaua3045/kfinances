package com.kaua.finances.application.usecases.bill.update;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.application.usecases.bill.output.UpdateBillOutput;

import java.util.Objects;

public class DefaultUpdateBillUseCase implements UpdateBillUseCase {

    private final BillGateway billGateway;

    public DefaultUpdateBillUseCase(final BillGateway billGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
    }

    @Override
    public Either<DomainException, UpdateBillOutput> execute(
            final String id,
            final String title,
            final String description,
            final boolean pending
    ) {
        final var aBill = this.billGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(Bill.class, id));

        aBill.update(title, description, pending);

        final var aBillValidate = aBill.validate();

        if (!aBillValidate.isEmpty()) {
            return Either.left(DomainException.with(aBillValidate));
        }

        return Either.right(UpdateBillOutput.from(this.billGateway.update(aBill).getId()));
    }
}
