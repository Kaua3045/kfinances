package com.kaua.finances.application.usecases.account.create;

import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.EmailAlreadyExistsException;
import com.kaua.finances.application.usecases.account.output.CreateAccountOutput;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountCacheGateway;
import com.kaua.finances.domain.account.AccountGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DefaultCreateAccountUseCase implements CreateAccountUseCase {

    private final AccountGateway accountGateway;
    private final AccountCacheGateway accountCacheGateway;

    private final Logger logger = LoggerFactory.getLogger(DefaultCreateAccountUseCase.class);

    private static final ThreadFactory THREAD_FACTORY = new CustomizableThreadFactory(
            "cache-account-");
    private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(4, THREAD_FACTORY);

    public DefaultCreateAccountUseCase(final AccountGateway accountGateway, AccountCacheGateway accountCacheGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.accountCacheGateway = Objects.requireNonNull(accountCacheGateway);
    }

    @Override
    public Either<DomainException, CreateAccountOutput> execute(String aName, String aEmail, String aPassword) {
        if (this.accountGateway.findByEmail(aEmail).isPresent()) {
            return Either.left(DomainException.with(EmailAlreadyExistsException.with()));
        }

        final var aAccount = Account.newAccount(aName, aEmail, aPassword);
        final var aAccountValidated = aAccount.validate();

        if (!aAccountValidated.isEmpty()) {
            return Either.left(DomainException.with(aAccountValidated));
        }

        CompletableFuture.supplyAsync(() -> {
            logger.info("CompletableFuture set account: {} in redis cache", aAccount.getId());
            return this.accountCacheGateway.create(aAccount);
        }, EXECUTORS);

        this.accountGateway.create(aAccount);

        return Either.right(CreateAccountOutput.from(aAccount.getId()));
    }
}
