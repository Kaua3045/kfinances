package com.kaua.finances.infrastructure.account.persistence.cache;

import org.springframework.data.repository.CrudRepository;

public interface AccountCacheRepository extends CrudRepository<AccountRedisEntity, String> {
}
