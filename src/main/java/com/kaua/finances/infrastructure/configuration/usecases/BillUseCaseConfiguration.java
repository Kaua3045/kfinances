package com.kaua.finances.infrastructure.configuration.usecases;

import com.kaua.finances.application.usecases.bill.CreateBillUseCase;
import com.kaua.finances.application.usecases.bill.DefaultCreateBillUseCase;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.bills.BillGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class BillUseCaseConfiguration {

    private final BillGateway billGateway;
    private final AccountGateway accountGateway;

    public BillUseCaseConfiguration(final BillGateway billGateway, final AccountGateway accountGateway) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Bean
    public CreateBillUseCase createBillUseCase() {
        return new DefaultCreateBillUseCase(billGateway, accountGateway);
    }
}
