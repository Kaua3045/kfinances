package com.kaua.finances.application.usecases.bill.output;

import com.kaua.finances.domain.bills.Bill;

import java.time.Instant;

public record BillListOutput(
        String id,
        String title,
        String description,
        boolean pending,
        Instant createdAt,
        Instant finishedDate
) {

    public static BillListOutput from(final Bill aBill) {
        return new BillListOutput(
                aBill.getId(),
                aBill.getTitle(),
                aBill.getDescription(),
                aBill.isPending(),
                aBill.getCreatedAt(),
                aBill.getFinishedDate()
        );
    }
}
