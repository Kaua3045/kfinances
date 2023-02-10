package com.kaua.finances.infrastructure.bill.models;

public record CreateBillRequest(
        String title,
        String description,
        boolean pending,
        String accountId
) {
}
