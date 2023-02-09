package com.kaua.finances.domain.bills;

import java.util.Optional;

public interface BillGateway {

    Bill create(Bill aBill);

    void deleteById(String id);

    Optional<Bill> findById(String id);

    Bill update(Bill aBill);
}
