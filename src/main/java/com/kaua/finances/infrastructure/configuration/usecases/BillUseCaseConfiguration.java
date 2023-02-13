package com.kaua.finances.infrastructure.configuration.usecases;

import com.kaua.finances.application.usecases.bill.create.CreateBillUseCase;
import com.kaua.finances.application.usecases.bill.create.DefaultCreateBillUseCase;
import com.kaua.finances.application.usecases.bill.delete.DefaultDeleteBillByIdUseCase;
import com.kaua.finances.application.usecases.bill.delete.DeleteBillByIdUseCase;
import com.kaua.finances.application.usecases.bill.retrieve.get.DefaultGetBillByIdUseCase;
import com.kaua.finances.application.usecases.bill.retrieve.get.GetBillByIdUseCase;
import com.kaua.finances.application.usecases.bill.retrieve.list.DefaultListBillByAccountIdUseCase;
import com.kaua.finances.application.usecases.bill.retrieve.list.ListBillByAccountIdUseCase;
import com.kaua.finances.application.usecases.bill.update.DefaultUpdateBillUseCase;
import com.kaua.finances.application.usecases.bill.update.DefaultUpdatePendingBillUseCase;
import com.kaua.finances.application.usecases.bill.update.UpdateBillUseCase;
import com.kaua.finances.application.usecases.bill.update.UpdatePendingBillUseCase;
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

    @Bean
    public GetBillByIdUseCase getBillByIdUseCase() {
        return new DefaultGetBillByIdUseCase(billGateway);
    }

    @Bean
    public ListBillByAccountIdUseCase listBillByAccountIdUseCase() {
        return new DefaultListBillByAccountIdUseCase(billGateway, accountGateway);
    }

    @Bean
    public UpdateBillUseCase updateBillUseCase() {
        return new DefaultUpdateBillUseCase(billGateway);
    }

    @Bean
    public UpdatePendingBillUseCase updatePendingBillUseCase() {
        return new DefaultUpdatePendingBillUseCase(billGateway);
    }

    @Bean
    public DeleteBillByIdUseCase deleteBillByIdUseCase() {
        return new DefaultDeleteBillByIdUseCase(billGateway);
    }
}
