package com.kaua.finances.domain.bills;

import com.kaua.finances.application.usecases.bill.output.BillOutput;

import java.util.Optional;

public interface BillCacheGateway {

    BillOutput create(Bill aBill);

    void deleteById(String anId);

    Optional<BillOutput> findById(String anId);

    BillOutput update(Bill aBill);
}
