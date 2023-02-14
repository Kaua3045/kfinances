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
import com.kaua.finances.domain.account.AccountRedisGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfiguration {

    private final AccountGateway accountGateway;
    private final AccountRedisGateway accountRedisGateway;

    public AccountUseCaseConfiguration(
            final AccountGateway accountGateway,
            final AccountRedisGateway accountRedisGateway
    ) {
        this.accountGateway = accountGateway;
        this.accountRedisGateway = accountRedisGateway;
    }

    @Bean
    public CreateAccountUseCase createAccountUseCase() {
        return new DefaultCreateAccountUseCase(accountGateway, accountRedisGateway);
    }

    @Bean
    public UpdateAccountUseCase updateAccountUseCase() {
        return new DefaultUpdateAccountUseCase(accountGateway);
    }

    @Bean
    public GetAccountByIdUseCase getAccountByIdUseCase() {
        return new DefaultGetAccountByIdUseCase(accountGateway, accountRedisGateway);
    }

    @Bean
    public DeleteAccountByIdUseCase deleteAccountByIdUseCase() {
        return new DefaultDeleteAccountByIdUseCase(accountGateway);
    }
}
