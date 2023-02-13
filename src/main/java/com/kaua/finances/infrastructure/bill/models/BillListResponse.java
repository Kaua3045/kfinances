package com.kaua.finances.infrastructure.bill.models;

import java.time.Instant;

public record BillListResponse(
        String id,
        String title,
        String description,
        boolean pending,
        Instant createdAt,
        Instant finishedDate
) {
}
