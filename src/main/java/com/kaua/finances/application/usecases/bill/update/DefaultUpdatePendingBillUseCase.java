package com.kaua.finances.application.usecases.bill.update;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillCacheGateway;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.application.usecases.bill.output.UpdateBillOutput;

import java.util.Objects;

public class DefaultUpdatePendingBillUseCase implements UpdatePendingBillUseCase {

    private final BillGateway billGateway;
    private final BillCacheGateway billCacheGateway;

    public DefaultUpdatePendingBillUseCase(final BillGateway billGateway, final BillCacheGateway billCacheGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.billCacheGateway = Objects.requireNonNull(billCacheGateway);
    }

    @Override
    public Either<DomainException, UpdateBillOutput> execute(String id, boolean pending) {
        final var aBill = this.billGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(Bill.class, id));

        aBill.update(
                aBill.getTitle(),
                aBill.getDescription(),
                pending
        );

        final var aBillValidate = aBill.validate();

        if (!aBillValidate.isEmpty()) {
            return Either.left(DomainException.with(aBillValidate));
        }

        this.billCacheGateway.create(aBill);

        return Either.right(UpdateBillOutput.from(this.billGateway.update(aBill).getId()));
    }
}
