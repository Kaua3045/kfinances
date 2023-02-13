package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.domain.bills.BillListOutput;
import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListBillByAccountIdUseCase implements ListBillByAccountIdUseCase {

    private final BillGateway billGateway;
    private final AccountGateway accountGateway;

    public DefaultListBillByAccountIdUseCase(final BillGateway billGateway, final AccountGateway accountGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Pagination<BillListOutput> execute(String accountId, SearchQuery aQuery) {
        final var accountFounded = this.accountGateway.findById(accountId)
                .orElseThrow(() -> NotFoundException.with(Account.class, accountId));

        return this.billGateway.findAllByAccountId(accountFounded.getId(), aQuery)
                .map(BillListOutput::from);
    }
}
