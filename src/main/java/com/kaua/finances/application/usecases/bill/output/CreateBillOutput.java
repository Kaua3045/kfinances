package com.kaua.finances.application.usecases.bill.output;

public record CreateBillOutput(String id) {

    public static CreateBillOutput from(final String id) {
        return new CreateBillOutput(id);
    }
}
