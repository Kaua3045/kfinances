package com.kaua.finances.application.usecases.bill.retrieve.get;

import com.kaua.finances.application.usecases.bill.output.BillOutput;

public interface GetBillByIdUseCase {

    BillOutput execute(String id);
}
