package com.kaua.finances.domain.bills;

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
}
