package com.kaua.finances.infrastructure.configuration.usecases;

import com.kaua.finances.application.usecases.security.CreateAuthenticateUseCase;
import com.kaua.finances.application.usecases.security.DefaultCreateAuthenticateUseCase;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.authenticate.SecurityGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticateUseCaseConfiguration {

    private final AccountGateway accountGateway;
    private final SecurityGateway securityGateway;

    public AuthenticateUseCaseConfiguration(AccountGateway accountGateway, SecurityGateway securityGateway) {
        this.accountGateway = accountGateway;
        this.securityGateway = securityGateway;
    }

    @Bean
    public CreateAuthenticateUseCase createAuthenticateUseCase() {
        return new DefaultCreateAuthenticateUseCase(accountGateway, securityGateway);
    }
}
