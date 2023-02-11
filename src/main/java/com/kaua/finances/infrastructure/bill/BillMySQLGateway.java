package com.kaua.finances.infrastructure.bill;

import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.infrastructure.bill.persistence.BillJpaFactory;
import com.kaua.finances.infrastructure.bill.persistence.BillRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class BillMySQLGateway implements BillGateway {

    private final BillRepository billRepository;

    public BillMySQLGateway(final BillRepository billRepository) {
        this.billRepository = Objects.requireNonNull(billRepository);
    }

    @Override
    public Bill create(Bill aBill) {
        final var entity = this.billRepository.save(BillJpaFactory.from(aBill));
        return BillJpaFactory.toDomain(entity);
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public Optional<Bill> findById(String id) {
        return this.billRepository.findById(id)
                .map(BillJpaFactory::toDomain);
    }

    @Override
    public Bill update(Bill aBill) {
        final var entity = this.billRepository.save(BillJpaFactory.from(aBill));
        return BillJpaFactory.toDomain(entity);
    }
}
