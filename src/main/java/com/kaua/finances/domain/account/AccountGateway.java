package com.kaua.finances.domain.account;

import java.util.Optional;

public interface AccountGateway {

    Account create(Account aAccount);

    void deleteById(String anId);

    Optional<Account> findById(String anId);

    Account update(Account aAccount);
}
