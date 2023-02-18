package com.kaua.finances.infrastructure.bill.persistence.cache;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@RedisHash("bill")
public class BillRedisEntity {

    @Id
    private String id;

    private String title;
    private String description;
    private boolean pending;
    private Instant finishedDate;
    private String accountId;

    public BillRedisEntity() {}

    public BillRedisEntity(
            String id,
            String title,
            String description,
            boolean pending,
            Instant finishedDate,
            String accountId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pending = pending;
        this.finishedDate = finishedDate;
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public Instant getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Instant finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
