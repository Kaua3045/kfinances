package com.kaua.finances.infrastructure.configuration.usecases;

import com.kaua.finances.application.usecases.account.*;
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

    @Bean
    public GetAccountByIdUseCase getAccountByIdUseCase() {
        return new DefaultGetAccountByIdUseCase(accountGateway);
    }

    @Bean
    public DeleteAccountByIdUseCase deleteAccountByIdUseCase() {
        return new DefaultDeleteAccountByIdUseCase(accountGateway);
    }
}
