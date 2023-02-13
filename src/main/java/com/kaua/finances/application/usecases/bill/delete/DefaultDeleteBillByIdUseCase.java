package com.kaua.finances.application.usecases.bill.delete;

import com.kaua.finances.domain.bills.BillGateway;

import java.util.Objects;

public class DefaultDeleteBillByIdUseCase implements DeleteBillByIdUseCase {

    private final BillGateway billGateway;

    public DefaultDeleteBillByIdUseCase(final BillGateway billGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
    }


    @Override
    public void execute(String id) {
        this.billGateway.deleteById(id);
    }
}
