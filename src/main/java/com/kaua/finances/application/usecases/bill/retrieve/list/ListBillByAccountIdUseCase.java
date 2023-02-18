package com.kaua.finances.application.usecases.bill.retrieve.list;

import com.kaua.finances.application.usecases.bill.output.BillListOutput;
import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;

public interface ListBillByAccountIdUseCase {

    Pagination<BillListOutput> execute(String accountId, SearchQuery aQuery);
}
