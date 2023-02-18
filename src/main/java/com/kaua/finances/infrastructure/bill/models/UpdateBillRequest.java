package com.kaua.finances.infrastructure.bill.models;

public record UpdateBillRequest(
        String title,
        String description,
        boolean pending
) {
}
