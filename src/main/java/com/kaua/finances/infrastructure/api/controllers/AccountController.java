package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.CreateAccountUseCase;
import com.kaua.finances.infrastructure.account.models.CreateAccountRequest;
import com.kaua.finances.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;

    public AccountController(final CreateAccountUseCase createAccountUseCase) {
        this.createAccountUseCase = Objects.requireNonNull(createAccountUseCase);
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
}
