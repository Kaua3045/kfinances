package com.kaua.finances.domain.account;

import com.kaua.finances.domain.utils.InstantUtils;
import com.kaua.finances.domain.utils.UuidUtils;
import com.kaua.finances.domain.validate.Error;
import com.kaua.finances.domain.validate.ValidateHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account implements ValidateHandler {

    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> bills;
    private Instant createdAt;
    private Instant updatedAt;

    private final int PASSWORD_MIN_LENGTH = 8;

    public Account(
            final String anId,
            final String aName,
            final String aEmail,
            final String aPassword,
            final List<String> aBills,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = anId;
        this.name = aName;
        this.email = aEmail;
        this.password = aPassword;
        this.bills = aBills;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Account newAccount(final String aName, final String aEmail, final String aPassword) {
        final var anId = UuidUtils.unique();
        final var now = InstantUtils.now();

        return new Account(
                anId,
                aName,
                aEmail,
                aPassword,
                new ArrayList<>(),
                now,
                now
        );
    }

    public static Account with(
            final String anId,
            final String aName,
            final String aEmail,
            final String aPassword,
            final List<String> aBills,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        return new Account(
                anId,
                aName,
                aEmail,
                aPassword,
                new ArrayList<>(aBills),
                createdAt,
                updatedAt
        );
    }

    public static Account with(final Account aAccount) {
        return with(
                aAccount.id,
                aAccount.name,
                aAccount.email,
                aAccount.password,
                aAccount.bills,
                aAccount.createdAt,
                aAccount.updatedAt
        );
    }

    public Account addBill(final String aBillId) {
        if (aBillId == null) {
            return this;
        }

        this.bills.add(aBillId);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Account addBills(final List<String> aBillId) {
        if (aBillId == null || aBillId.isEmpty()) {
            return this;
        }

        this.bills.addAll(aBillId);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Account removeBill(final String aBillId) {
        if (aBillId == null) {
            return this;
        }

        this.bills.remove(aBillId);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Account update(final String aName, final String aPassword, final List<String> aBills) {
        this.name = aName;
        this.password = aPassword;
        this.bills = new ArrayList<>(aBills != null ? aBills : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public List<Error> validate() {
        final var errors = new ArrayList<Error>();

        if (name == null || name.isBlank()) {
            errors.add(new Error("'name' should not be empty or null"));
        }

        if (password == null || password.isBlank()) {
            errors.add(new Error("'password' should not be empty or null"));
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            errors.add(new Error("'password' must contain 8 characters at least"));
        }

        if (email == null || email.isBlank()) {
            errors.add(new Error("'email' should not be empty or null"));
        }

        return errors;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getBills() {
        return bills;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
