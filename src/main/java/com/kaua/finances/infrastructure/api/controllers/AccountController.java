package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.account.create.CreateAccountUseCase;
import com.kaua.finances.application.usecases.account.delete.DeleteAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.retrieve.GetAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.update.UpdateAccountUseCase;
import com.kaua.finances.infrastructure.account.models.CreateAccountRequest;
import com.kaua.finances.infrastructure.account.models.UpdateAccountRequest;
import com.kaua.finances.infrastructure.api.AccountAPI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AccountController implements AccountAPI {

    private final CreateAccountUseCase createAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final DeleteAccountByIdUseCase deleteAccountByIdUseCase;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountController(
            final CreateAccountUseCase createAccountUseCase,
            final UpdateAccountUseCase updateAccountUseCase,
            final GetAccountByIdUseCase getAccountByIdUseCase,
            final DeleteAccountByIdUseCase deleteAccountByIdUseCase,
            final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.createAccountUseCase = Objects.requireNonNull(createAccountUseCase);
        this.updateAccountUseCase = Objects.requireNonNull(updateAccountUseCase);
        this.getAccountByIdUseCase = Objects.requireNonNull(getAccountByIdUseCase);
        this.deleteAccountByIdUseCase = Objects.requireNonNull(deleteAccountByIdUseCase);
        this.bCryptPasswordEncoder = Objects.requireNonNull(bCryptPasswordEncoder);
    }

    @Override
    public ResponseEntity<?> createAccount(CreateAccountRequest input) {
        final var aAccount = this.createAccountUseCase.execute(
                input.name(),
                input.email(),
                bCryptPasswordEncoder.encode(input.password())
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
                bCryptPasswordEncoder.encode(input.password())
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
