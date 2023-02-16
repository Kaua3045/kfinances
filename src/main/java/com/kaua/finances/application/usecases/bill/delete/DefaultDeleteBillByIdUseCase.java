package com.kaua.finances.application.usecases.bill.delete;

import com.kaua.finances.domain.bills.BillCacheGateway;
import com.kaua.finances.domain.bills.BillGateway;

import java.util.Objects;

public class DefaultDeleteBillByIdUseCase implements DeleteBillByIdUseCase {

    private final BillGateway billGateway;
    private final BillCacheGateway billCacheGateway;

    public DefaultDeleteBillByIdUseCase(final BillGateway billGateway, final BillCacheGateway billCacheGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.billCacheGateway = Objects.requireNonNull(billCacheGateway);
    }


    @Override
    public void execute(String id) {
        this.billGateway.deleteById(id);
        this.billCacheGateway.deleteById(id);
    }
}
