package com.kaua.finances.application.usecases.bill.retrieve.get;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillCacheGateway;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.application.usecases.bill.output.BillOutput;

import java.util.Objects;

public class DefaultGetBillByIdUseCase implements GetBillByIdUseCase {

    private final BillGateway billGateway;
    private final BillCacheGateway billCacheGateway;

    public DefaultGetBillByIdUseCase(final BillGateway billGateway, BillCacheGateway billCacheGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.billCacheGateway = billCacheGateway;
    }

    @Override
    public BillOutput execute(String id) {
        final var aBillCache = this.billCacheGateway.findById(id);

        if (aBillCache.isEmpty()) {
            final var aBillDatabase = this.billGateway.findById(id)
                    .orElseThrow(() -> NotFoundException.with(Bill.class, id));

            this.billCacheGateway.create(aBillDatabase);

            return BillOutput.from(aBillDatabase);
        }
//        final var aBill = this.billGateway.findById(id)
//                .orElseThrow(() -> NotFoundException.with(Bill.class, id));

        return aBillCache.map((bill) -> BillOutput.from(
                bill.id(),
                bill.title(),
                bill.description(),
                bill.pending(),
                bill.finishedDate(),
                bill.accountId()
        )).get();
    }
}
