package com.kaua.finances.infrastructure.account.models;

public record AuthenticateTokenOutput(
        String token
) {

    public static AuthenticateTokenOutput from(String aToken) {
        return new AuthenticateTokenOutput(aToken);
    }
}
