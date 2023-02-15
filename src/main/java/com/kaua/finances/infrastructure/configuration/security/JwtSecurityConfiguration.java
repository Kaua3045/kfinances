package com.kaua.finances.infrastructure.configuration.security;

import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.authenticate.SecurityGateway;
import com.kaua.finances.infrastructure.security.jwt.JwtAuthenticationFilter;
import com.kaua.finances.infrastructure.security.jwt.JwtServiceGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecurityConfiguration {

    @Value("${spring.jwt.secret}")
    private String SECRET;

    @Value("${spring.jwt.expire}")
    private String EXPIRATION;

    @Bean
    public SecurityGateway jwtServiceGateway() {
        return new JwtServiceGateway(SECRET, EXPIRATION);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AccountGateway accountGateway, AccountCacheGateway accountCacheGateway) {
        return new JwtAuthenticationFilter(jwtServiceGateway(), accountGateway, accountCacheGateway);
    }
}
