package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.application.usecases.bill.output.BillOutput;

import java.util.Objects;

public class DefaultGetBillByIdUseCase implements GetBillByIdUseCase {

    private final BillGateway billGateway;

    public DefaultGetBillByIdUseCase(final BillGateway billGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
    }

    @Override
    public BillOutput execute(String id) {
        final var aBill = this.billGateway.findById(id)
                .orElseThrow(() -> NotFoundException.with(Bill.class, id));

        return BillOutput.from(aBill);
    }
}
