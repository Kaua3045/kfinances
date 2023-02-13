package com.kaua.finances.infrastructure.bill.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillJpaEntity, String> {

    Page<BillJpaEntity> findAllByAccountId(String id, Pageable page);
}
