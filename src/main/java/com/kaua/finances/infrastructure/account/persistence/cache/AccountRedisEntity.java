package com.kaua.finances.infrastructure.account.persistence.cache;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "account", timeToLive = 259200)
public class AccountRedisEntity {

    @Id
    private String id;

    private String name;
    private String email;

    public AccountRedisEntity() {}

    public AccountRedisEntity(
            String id,
            String name,
            String email
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
