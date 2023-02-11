package com.kaua.finances.infrastructure.account.models;

public record AuthenticateRequest(
        String email,
        String password
) {
}
