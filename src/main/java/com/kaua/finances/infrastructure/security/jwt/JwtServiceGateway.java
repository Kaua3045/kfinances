package com.kaua.finances.infrastructure.security.jwt;

import com.kaua.finances.domain.authenticate.SecurityGateway;
import com.kaua.finances.domain.utils.InstantUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtServiceGateway implements SecurityGateway {

    private final String SECRET_KEY_JWT;
    private int JWT_EXPIRATION_TIME = 24;
    private ChronoUnit JWT_EXPIRATION_UNIT_SPECIFIC = ChronoUnit.HOURS;

    public JwtServiceGateway(String SECRET_KEY_JWT) {
        this.SECRET_KEY_JWT = SECRET_KEY_JWT;
    }

    @Override
    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String generateToken(String id) {
        return Jwts
                .builder()
                .setSubject(id)
                .setIssuedAt(Date.from(InstantUtils.now()))
                .setExpiration(Date.from(InstantUtils.now().plus(JWT_EXPIRATION_TIME, JWT_EXPIRATION_UNIT_SPECIFIC)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, String id) {
        final var accountId = extractSubject(token);
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
