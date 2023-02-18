package com.kaua.finances.infrastructure.bill;

import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;
import com.kaua.finances.infrastructure.bill.persistence.BillJpaEntity;
import com.kaua.finances.infrastructure.bill.persistence.BillJpaFactory;
import com.kaua.finances.infrastructure.bill.persistence.BillRepository;
import com.kaua.finances.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        if (this.billRepository.findById(id).isPresent()) {
            this.billRepository.deleteById(id);
        }
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

    @Override
    public Pagination<Bill> findAllByAccountId(String id, SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var pageResult = this.billRepository.findAllByAccountId(id, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(BillJpaFactory::toDomain).toList()
        );
    }

}
