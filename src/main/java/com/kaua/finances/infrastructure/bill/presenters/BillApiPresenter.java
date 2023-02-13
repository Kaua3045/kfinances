package com.kaua.finances.infrastructure.bill.presenters;

import com.kaua.finances.domain.bills.BillListOutput;
import com.kaua.finances.infrastructure.bill.models.BillListResponse;

public interface BillApiPresenter {

    static BillListResponse present(final BillListOutput output) {
        return new BillListResponse(
                output.id(),
                output.title(),
                output.description(),
                output.pending(),
                output.createdAt(),
                output.finishedDate()
        );
    }
}
