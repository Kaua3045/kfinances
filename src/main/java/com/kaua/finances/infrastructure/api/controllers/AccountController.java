package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.account.CreateAccountUseCase;
import com.kaua.finances.application.usecases.account.DeleteAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.GetAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.UpdateAccountUseCase;
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
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final DeleteAccountByIdUseCase deleteAccountByIdUseCase;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final UpdateAccountUseCase updateAccountUseCase,
            final GetAccountByIdUseCase getAccountByIdUseCase,
            final DeleteAccountByIdUseCase deleteAccountByIdUseCase) {
        this.createAccountUseCase = Objects.requireNonNull(createAccountUseCase);
        this.updateAccountUseCase = Objects.requireNonNull(updateAccountUseCase);
        this.getAccountByIdUseCase = Objects.requireNonNull(getAccountByIdUseCase);
        this.deleteAccountByIdUseCase = Objects.requireNonNull(deleteAccountByIdUseCase);
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
    public ResponseEntity<?> getById(String id) {
        final var aAccount = this.getAccountByIdUseCase.execute(id);

        return ResponseEntity.ok().body(aAccount);
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

    @Override
    public void deleteById(String id) {
        this.deleteAccountByIdUseCase.execute(id);
    }
}
