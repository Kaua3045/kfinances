package com.kaua.finances.domain.authenticate;

public interface SecurityGateway {

    String extractSubject(String token);

    String generateToken(String subject);

    boolean isTokenValid(String token, String id);

    boolean isTokenExpired(String token);
}
