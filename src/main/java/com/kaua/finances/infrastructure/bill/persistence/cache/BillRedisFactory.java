package com.kaua.finances.infrastructure.bill.persistence.cache;

import com.kaua.finances.application.usecases.bill.output.BillOutput;
import com.kaua.finances.domain.bills.Bill;

public class BillRedisFactory {

    public static BillRedisEntity from(final Bill aBill) {
        return new BillRedisEntity(
                aBill.getId(),
                aBill.getTitle(),
                aBill.getDescription(),
                aBill.isPending(),
                aBill.getFinishedDate(),
                aBill.getAccountId().getId()
        );
    }

    public static BillOutput toOutput(final BillRedisEntity aBillRedis) {
        return BillOutput.from(
                aBillRedis.getId(),
                aBillRedis.getTitle(),
                aBillRedis.getDescription(),
                aBillRedis.isPending(),
                aBillRedis.getFinishedDate(),
                aBillRedis.getAccountId()
        );
    }
}
