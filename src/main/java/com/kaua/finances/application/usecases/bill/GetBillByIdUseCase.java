package com.kaua.finances.application.usecases.bill;

import com.kaua.finances.application.usecases.bill.output.BillOutput;

public interface GetBillByIdUseCase {

    BillOutput execute(String id);
}
