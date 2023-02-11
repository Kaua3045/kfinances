package com.kaua.finances.infrastructure.configuration.security;

import com.kaua.finances.infrastructure.security.jwt.JwtServiceGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecurityConfiguration {

    @Value("{spring.jwt.secret}")
    private String SECRET;

    @Bean
    public JwtServiceGateway jwtServiceGateway() {
        System.out.println(SECRET);
        return new JwtServiceGateway(SECRET);
    }
}
