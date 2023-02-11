package com.kaua.finances.infrastructure.api.controllers;

import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.infrastructure.account.models.AuthenticateRequest;
import com.kaua.finances.infrastructure.account.models.AuthenticateTokenOutput;
import com.kaua.finances.infrastructure.api.AuthAPI;
import com.kaua.finances.infrastructure.security.jwt.JwtGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController implements AuthAPI {

    private final AccountGateway accountGateway;
    private final JwtGateway jwtGateway;
    private final AuthenticationManager authenticationManager;

    public AuthenticateController(
            final AccountGateway accountGateway,
            final JwtGateway jwtGateway,
            final AuthenticationManager authenticationManager
    ) {
        this.accountGateway = accountGateway;
        this.jwtGateway = jwtGateway;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticateRequest input) {
        final var aAccount = this.accountGateway.findByEmail(input.email()).get();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.email(),
                input.password()
        ));

        final var aToken = jwtGateway.generateToken(aAccount.getId());

        return ResponseEntity.ok().body(AuthenticateTokenOutput.from(aToken));
    }
}
