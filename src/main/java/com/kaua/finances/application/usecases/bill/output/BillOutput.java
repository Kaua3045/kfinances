package com.kaua.finances.application.usecases.bill.output;

import com.kaua.finances.domain.bills.Bill;

import java.time.Instant;

public record BillOutput(
        String id,
        String title,
        String description,
        boolean pending,
        Instant finishedDate,
        String accountId
) {

    public static BillOutput from(final Bill aBill) {
        return new BillOutput(
                aBill.getId(),
                aBill.getTitle(),
                aBill.getDescription(),
                aBill.isPending(),
                aBill.getFinishedDate(),
                aBill.getAccountId().getId()
        );
    }

    public static BillOutput from(
            final String id,
            final String title,
            final String description,
            final boolean pending,
            final Instant finishedDate,
            final String accountId
    ) {
        return new BillOutput(
                id,
                title,
                description,
                pending,
                finishedDate,
                accountId
        );
    }
}
