package com.kaua.finances.domain.bills;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.utils.InstantUtils;
import com.kaua.finances.domain.utils.UuidUtils;
import com.kaua.finances.domain.validate.Error;
import com.kaua.finances.domain.validate.ValidateHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Bill implements ValidateHandler {

    private String id;
    private Account accountId;
    private String title;
    private String description;
    private boolean pending;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant finishedDate;

    private static final int DESCRIPTION_MAX_LENGTH = 3000;

    public Bill(
            final String id,
            final Account accountId,
            final String title,
            final String description,
            final boolean pending,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant finishedDate
    ) {
        this.id = id;
        this.accountId = accountId;
        this.title = title;
        this.description = description;
        this.pending = pending;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.finishedDate = finishedDate;
    }

    public static Bill newBill(
            final Account accountId,
            final String title,
            final String description,
            final boolean pending
    ) {
        final var billId = UuidUtils.unique();
        final var now = InstantUtils.now();
        final var finishedDate = pending ? null : now;

        return new Bill(
                billId,
                accountId,
                title,
                description,
                pending,
                now,
                now,
                finishedDate
        );
    }

    public static Bill with(
            final String id,
            final Account accountId,
            final String title,
            final String description,
            final boolean pending,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant finishedDate
    ) {
        return new Bill(
                id,
                accountId,
                title,
                description,
                pending,
                createdAt,
                updatedAt,
                finishedDate
        );
    }

    public static Bill with(final Bill aBill) {
        return new Bill(
                aBill.id,
                aBill.accountId,
                aBill.title,
                aBill.description,
                aBill.pending,
                aBill.createdAt,
                aBill.updatedAt,
                aBill.finishedDate
        );
    }

    public Bill update(final String title, final String description, final boolean pending) {
        if (pending) {
            enableBill();
        } else {
            disableBill();
        }

        this.title = title;
        this.description = description;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Bill enableBill() {
        this.pending = true;
        this.updatedAt = InstantUtils.now();
        this.finishedDate = null;
        return this;
    }

    public Bill disableBill() {
        if (getFinishedDate() == null) {
            this.finishedDate = InstantUtils.now();
        }

        this.pending = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public List<Error> validate() {
        List<Error> errors = new ArrayList<>();

        if (accountId == null || accountId.getId() == null) {
            errors.add(new Error("'accountId' should not be empty or null"));
        }

        if (title == null || title.isBlank()) {
            errors.add(new Error("'title' should not be empty or null"));
        }

        if (description != null && description.length() > DESCRIPTION_MAX_LENGTH) {
            errors.add(new Error("'description' must be between 3000 characters"));
        }

        return errors;
    }

    public String getId() {
        return id;
    }

    public Account getAccountId() {
        return accountId;
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
}
