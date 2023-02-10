package com.kaua.finances.infrastructure.bill.persistence;

import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.infrastructure.account.persistence.AccountJpaFactory;

public class BillJpaFactory {

    public static BillJpaEntity from(final Bill aBill) {
        return new BillJpaEntity(
                aBill.getId(),
                aBill.getTitle(),
                aBill.getDescription(),
                aBill.isPending(),
                aBill.getCreatedAt(),
                aBill.getUpdatedAt(),
                aBill.getFinishedDate(),
                AccountJpaFactory.from(aBill.getAccountId())
        );
    }

    public static Bill toDomain(final BillJpaEntity aBillJpa) {
        return new Bill(
                aBillJpa.getId(),
                AccountJpaFactory.toDomain(aBillJpa.getAccount()),
                aBillJpa.getTitle(),
                aBillJpa.getDescription(),
                aBillJpa.isPending(),
                aBillJpa.getCreatedAt(),
                aBillJpa.getUpdatedAt(),
                aBillJpa.getFinishedDate()
        );
    }
}