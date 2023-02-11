package com.kaua.finances.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtServiceGateway implements JwtGateway {

    private final String SECRET_KEY_JWT;
    private int JWT_EXPIRATION_TIME = 24;

    public JwtServiceGateway(String SECRET_KEY_JWT) {
        this.SECRET_KEY_JWT = SECRET_KEY_JWT;
    }

    @Override
    public String extractId(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String generateToken(String id) {
        return Jwts
                .builder()
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() * 60 * JWT_EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, String id) {
        final var accountId = extractId(token);
        return accountId.equals(id) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY_JWT);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
