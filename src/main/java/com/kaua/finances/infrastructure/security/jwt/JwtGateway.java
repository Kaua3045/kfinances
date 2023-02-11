package com.kaua.finances.infrastructure.security.jwt;

public interface JwtGateway {

    String extractId(String token);

    String generateToken(String id);

    boolean isTokenValid(String token, String id);
}
