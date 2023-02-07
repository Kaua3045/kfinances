package com.kaua.finances.infrastructure.configuration.usecases;

import com.kaua.finances.application.usecases.CreateAccountUseCase;
import com.kaua.finances.application.usecases.DefaultCreateAccountUseCase;
import com.kaua.finances.application.usecases.DefaultUpdateAccountUseCase;
import com.kaua.finances.application.usecases.UpdateAccountUseCase;
import com.kaua.finances.domain.account.AccountGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfiguration {

    private final AccountGateway accountGateway;

    public AccountUseCaseConfiguration(final AccountGateway accountGateway) {
        this.accountGateway = accountGateway;
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase() {
        return new DefaultCreateAccountUseCase(accountGateway);
    }

    @Bean
    public UpdateAccountUseCase updateAccountUseCase() {
        return new DefaultUpdateAccountUseCase(accountGateway);
    }
}
