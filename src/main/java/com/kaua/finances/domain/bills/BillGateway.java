package com.kaua.finances.domain.bills;

import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;

import java.util.Optional;

public interface BillGateway {

    Bill create(Bill aBill);

    void deleteById(String id);

    Optional<Bill> findById(String id);

    Bill update(Bill aBill);

    Pagination<Bill> findAllByAccountId(String id, SearchQuery aQuery);
}
