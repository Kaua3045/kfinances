package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.application.usecases.security.CreateAuthenticateUseCase;
import com.kaua.finances.infrastructure.account.models.AuthenticateRequest;
import com.kaua.finances.infrastructure.api.AuthAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController implements AuthAPI {

    private final CreateAuthenticateUseCase createAuthenticateUseCase;
    private final AuthenticationManager authenticationManager;

    public AuthenticateController(
            final CreateAuthenticateUseCase createAuthenticateUseCase,
            final AuthenticationManager authenticationManager
    ) {
        this.createAuthenticateUseCase = createAuthenticateUseCase;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticateRequest input) {
        final var aToken = this.createAuthenticateUseCase.execute(input.email(), input.password());

        if (aToken.isLeft()) {
            throw aToken.getLeft();
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.email(),
                input.password()
        ));

        return ResponseEntity.ok(aToken.getRight());
    }
}
