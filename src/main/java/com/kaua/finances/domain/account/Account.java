package com.kaua.finances.domain.account;

import com.kaua.finances.domain.utils.InstantUtils;
import com.kaua.finances.domain.utils.UuidUtils;
import com.kaua.finances.domain.validate.Error;
import com.kaua.finances.domain.validate.ValidateHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Account implements ValidateHandler {

    private String id;
    private String name;
    private String email;
    private String password;
    private Instant createdAt;
    private Instant updatedAt;

    private final int PASSWORD_MIN_LENGTH = 8;

    public Account(
            final String anId,
            final String aName,
            final String aEmail,
            final String aPassword,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = anId;
        this.name = aName;
        this.email = aEmail;
        this.password = aPassword;
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
                now,
                now
        );
    }

    @Override
    public List<Error> validate() {
        final var errors = new ArrayList<Error>();

        if (name == null) {
            errors.add(new Error("'name' should not be null"));
            return errors;
        }

        if (name.isBlank()) {
            errors.add(new Error("'name' should not be empty"));
            return errors;
        }

        if (password == null) {
            errors.add(new Error("'password' should not be null"));
            return errors;
        }

        if (password.isBlank()) {
            errors.add(new Error("'password' should not be empty"));
            return errors;
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            errors.add(new Error("'password' Password must contain 8 characters at least"));
            return errors;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
