package com.kaua.finances.application.usecases.bill.output;

public record UpdateBillOutput(String id) {

    public static UpdateBillOutput from(final String id) {
        return new UpdateBillOutput(id);
    }
}
