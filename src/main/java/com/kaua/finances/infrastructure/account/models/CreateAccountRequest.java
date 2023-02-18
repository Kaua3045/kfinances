package com.kaua.finances.infrastructure.account.models;

public record CreateAccountRequest(
        String name,
        String email,
        String password
) {
}
