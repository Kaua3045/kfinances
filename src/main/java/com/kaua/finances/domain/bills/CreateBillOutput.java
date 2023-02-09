package com.kaua.finances.domain.bills;

public record CreateBillOutput(String id) {

    public static CreateBillOutput from(final String id) {
        return new CreateBillOutput(id);
    }
}
