package com.kaua.finances.infrastructure.configuration.usecases;

import com.kaua.finances.application.usecases.account.create.CreateAccountUseCase;
import com.kaua.finances.application.usecases.account.create.DefaultCreateAccountUseCase;
import com.kaua.finances.application.usecases.account.delete.DefaultDeleteAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.delete.DeleteAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.retrieve.DefaultGetAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.retrieve.GetAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.update.DefaultUpdateAccountUseCase;
import com.kaua.finances.application.usecases.account.update.UpdateAccountUseCase;
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
