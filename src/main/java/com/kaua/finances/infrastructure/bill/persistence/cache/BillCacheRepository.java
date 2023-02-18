package com.kaua.finances.infrastructure.bill.persistence.cache;

import org.springframework.data.repository.CrudRepository;

public interface BillCacheRepository extends CrudRepository<BillRedisEntity, String> {
}
