package com.kaua.finances.domain.account;

import com.kaua.finances.application.usecases.account.output.AccountOutput;

import java.util.Optional;

public interface AccountCacheGateway {

    AccountOutput create(Account aAccount);

    void deleteById(String anId);

    Optional<AccountOutput> findById(String anId);

    AccountOutput update(Account aAccount);
}
