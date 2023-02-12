package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.domain.bills.BillListOutput;
import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListBillByAccountIdUseCase implements ListBillByAccountIdUseCase {

    private final BillGateway billGateway;

    public DefaultListBillByAccountIdUseCase(final BillGateway billGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
    }

    @Override
    public Pagination<BillListOutput> execute(String accountId, SearchQuery aQuery) {
        return this.billGateway.findAllByAccountId(accountId, aQuery)
                .map(BillListOutput::from);
    }
}
