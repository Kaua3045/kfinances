package com.kaua.finances.infrastructure.account.persistence.cache;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@RedisHash("account")
public class AccountRedisEntity {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastGetDate;

    public AccountRedisEntity() {}

    public AccountRedisEntity(
            String id,
            String name,
            String email,
            String password,
            Instant createdAt,
            Instant updatedAt,
            Instant lastGetDate
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastGetDate = lastGetDate;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getLastGetDate() {
        return lastGetDate;
    }

    public void setLastGetDate(Instant lastGetDate) {
        this.lastGetDate = lastGetDate;
    }
}
