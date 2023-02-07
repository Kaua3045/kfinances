package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.CreateAccountUseCase;
import com.kaua.finances.application.usecases.UpdateAccountUseCase;
import com.kaua.finances.infrastructure.account.models.CreateAccountRequest;
import com.kaua.finances.infrastructure.account.models.UpdateAccountRequest;
import com.kaua.finances.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final UpdateAccountUseCase updateAccountUseCase) {
        this.createAccountUseCase = Objects.requireNonNull(createAccountUseCase);
        this.updateAccountUseCase = Objects.requireNonNull(updateAccountUseCase);
    }

    @Override
    public ResponseEntity<?> createAccount(CreateAccountRequest input) {
        final var aAccount = this.createAccountUseCase.execute(
                input.name(),
                input.email(),
                input.password()
        );

        if (aAccount.isLeft()) {
            throw aAccount.getLeft();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(aAccount.getRight());
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateAccountRequest input) {
        final var aAccount = this.updateAccountUseCase.execute(
                id,
                input.name(),
                input.password(),
                input.bills()
        );

        if (aAccount.isLeft()) {
            throw aAccount.getLeft();
        }

        return ResponseEntity.ok().body(aAccount.getRight());
    }
}
