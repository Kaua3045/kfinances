package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.domain.bills.BillOutput;

public interface GetBillByIdUseCase {

    BillOutput execute(String id);
}
