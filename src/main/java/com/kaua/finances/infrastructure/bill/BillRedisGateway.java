package com.kaua.finances.infrastructure.bill;

import com.kaua.finances.application.usecases.bill.output.BillOutput;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillCacheGateway;
import com.kaua.finances.infrastructure.bill.persistence.cache.BillCacheRepository;
import com.kaua.finances.infrastructure.bill.persistence.cache.BillRedisFactory;

import java.util.Optional;

public class BillRedisGateway implements BillCacheGateway {

    private final BillCacheRepository billCacheRepository;

    public BillRedisGateway(BillCacheRepository billCacheRepository) {
        this.billCacheRepository = billCacheRepository;
    }

    @Override
    public BillOutput create(Bill aBill) {
        final var aBillRedis = this.billCacheRepository.save(BillRedisFactory.from(aBill));
        return BillRedisFactory.toOutput(aBillRedis);
    }

    @Override
    public void deleteById(String anId) {
        if (this.billCacheRepository.findById(anId).isPresent()) {
            this.billCacheRepository.deleteById(anId);
        }
    }

    @Override
    public Optional<BillOutput> findById(String anId) {
        return this.billCacheRepository.findById(anId)
                .map((bill) -> BillOutput.from(
                        bill.getId(),
                        bill.getTitle(),
                        bill.getDescription(),
                        bill.isPending(),
                        bill.getFinishedDate(),
                        bill.getAccountId()
                ));
    }

    @Override
    public BillOutput update(Bill aBill) {
        final var aBillRedis = this.billCacheRepository.save(BillRedisFactory.from(aBill));
        return BillRedisFactory.toOutput(aBillRedis);
    }
}
