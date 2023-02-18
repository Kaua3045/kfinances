package com.kaua.finances.infrastructure.configuration.usecases;

import com.kaua.finances.application.usecases.account.retrieve.GetAccountByIdUseCase;
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
import com.kaua.finances.domain.bills.BillCacheGateway;
import com.kaua.finances.domain.bills.BillGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class BillUseCaseConfiguration {

    private final BillGateway billGateway;
    private final BillCacheGateway billCacheGateway;
    private final AccountGateway accountGateway;
    private final GetAccountByIdUseCase getAccountByIdUseCase;

    public BillUseCaseConfiguration(
            final BillGateway billGateway,
            final BillCacheGateway billCacheGateway,
            final AccountGateway accountGateway,
            final GetAccountByIdUseCase getAccountByIdUseCase) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.billCacheGateway = Objects.requireNonNull(billCacheGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.getAccountByIdUseCase = Objects.requireNonNull(getAccountByIdUseCase);
    }

    @Bean
    public CreateBillUseCase createBillUseCase() {
        return new DefaultCreateBillUseCase(billGateway, billCacheGateway, accountGateway);
    }

    @Bean
    public GetBillByIdUseCase getBillByIdUseCase() {
        return new DefaultGetBillByIdUseCase(billGateway, billCacheGateway);
    }

    @Bean
    public ListBillByAccountIdUseCase listBillByAccountIdUseCase() {
        return new DefaultListBillByAccountIdUseCase(billGateway, billCacheGateway, getAccountByIdUseCase);
    }

    @Bean
    public UpdateBillUseCase updateBillUseCase() {
        return new DefaultUpdateBillUseCase(billGateway, billCacheGateway);
    }

    @Bean
    public UpdatePendingBillUseCase updatePendingBillUseCase() {
        return new DefaultUpdatePendingBillUseCase(billGateway, billCacheGateway);
    }

    @Bean
    public DeleteBillByIdUseCase deleteBillByIdUseCase() {
        return new DefaultDeleteBillByIdUseCase(billGateway, billCacheGateway);
    }
}
