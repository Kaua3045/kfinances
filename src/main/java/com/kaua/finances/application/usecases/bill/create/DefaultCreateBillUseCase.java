package com.kaua.finances.application.usecases.bill.create;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillCacheGateway;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.application.usecases.bill.output.CreateBillOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DefaultCreateBillUseCase implements CreateBillUseCase {

    private final BillGateway billGateway;
    private final BillCacheGateway billCacheGateway;
    private final AccountGateway accountGateway;

    private static final ThreadFactory THREAD_FACTORY = new CustomizableThreadFactory(
            "cache-bill-");
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4, THREAD_FACTORY);

    private static final Logger logger = LoggerFactory.getLogger(DefaultCreateBillUseCase.class);

    public DefaultCreateBillUseCase(
            final BillGateway billGateway,
            final BillCacheGateway billCacheGateway,
            final AccountGateway accountGateway
    ) {
        this.billGateway = Objects.requireNonNull(billGateway);
        this.billCacheGateway = Objects.requireNonNull(billCacheGateway);
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Either<DomainException, CreateBillOutput> execute(
            final String accountId,
            final String title,
            final String description,
            final boolean pending
    ) {
        final var accountValid = this.accountGateway.findById(accountId)
                .orElseThrow(() -> NotFoundException.with(Account.class, accountId));

        final var aBill = Bill.newBill(accountValid, title, description, pending);
        final var aBillValidate = aBill.validate();

        if (!aBillValidate.isEmpty()) {
            return Either.left(DomainException.with(aBillValidate));
        }

        CompletableFuture.supplyAsync(() -> {
            logger.info("Set bill: {} in redis cache", aBill.getId());
            return this.billCacheGateway.create(aBill);
        }, EXECUTOR);

        return Either.right(CreateBillOutput.from(this.billGateway.create(aBill).getId()));
    }
}
