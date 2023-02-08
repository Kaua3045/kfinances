package com.kaua.finances.application.usecases;

import com.kaua.finances.domain.account.AccountOutput;

public interface GetAccountByIdUseCase {

    AccountOutput execute(String id);
}
