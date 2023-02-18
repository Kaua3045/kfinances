package com.kaua.finances.application.usecases.account.retrieve;

import com.kaua.finances.application.usecases.account.output.AccountOutput;

public interface GetAccountByIdUseCase {

    AccountOutput execute(String id);
}
