package com.kaua.finances.application.usecases.bill.retrieve.list;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.account.retrieve.GetAccountByIdUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.bills.BillCacheGateway;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.application.usecases.bill.output.BillListOutput;
import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListBillByAccountIdUseCase implements ListBillByAccountIdUseCase {

    private final BillGateway billGateway;
    private final BillCacheGateway billCacheGateway;
    private final GetAccountByIdUseCase getAccountByIdUseCase;

    public DefaultListBillByAccountIdUseCase(
            final BillGateway billGateway,
            final BillCacheGateway billCacheGateway,
            final GetAccountByIdUseCase getAccountByIdUseCase
    ) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.billCacheGateway = Objects.requireNonNull(billCacheGateway);
        this.getAccountByIdUseCase = Objects.requireNonNull(getAccountByIdUseCase);
    }

    @Override
    public Pagination<BillListOutput> execute(String accountId, SearchQuery aQuery) {
        final var accountFounded = this.getAccountByIdUseCase.execute(accountId);

        return this.billGateway.findAllByAccountId(accountFounded.id(), aQuery)
                .map(BillListOutput::from);
    }
}
