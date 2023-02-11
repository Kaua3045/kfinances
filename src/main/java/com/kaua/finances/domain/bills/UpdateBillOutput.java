package com.kaua.finances.domain.bills;

public record UpdateBillOutput(String id) {

    public static UpdateBillOutput from(final String id) {
        return new UpdateBillOutput(id);
    }
}
