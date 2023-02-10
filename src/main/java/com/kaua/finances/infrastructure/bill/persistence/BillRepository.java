package com.kaua.finances.infrastructure.bill.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<BillJpaEntity, String> {
}
