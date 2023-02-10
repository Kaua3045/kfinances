package com.kaua.finances.infrastructure.bill.persistence;

import com.kaua.finances.infrastructure.account.persistence.AccountJpaEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "bill")
@Table(name = "bills")
public class BillJpaEntity {

    @Id
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 3000)
    private String description;

    @Column(name = "pending", nullable = false)
    private boolean pending;

    @Column(name = "createdAt", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updatedAt", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "finishedDate", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant finishedDate;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountJpaEntity account;

    public BillJpaEntity() {
    }

    public BillJpaEntity(
            final String id,
            final String title,
            final String description,
            final boolean pending,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant finishedDate,
            final AccountJpaEntity account
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pending = pending;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.finishedDate = finishedDate;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPending() {
        return pending;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getFinishedDate() {
        return finishedDate;
    }

    public AccountJpaEntity getAccount() {
        return account;
    }
}
