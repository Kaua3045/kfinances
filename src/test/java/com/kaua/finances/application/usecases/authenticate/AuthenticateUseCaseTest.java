package com.kaua.finances.application.usecases.authenticate;

import com.kaua.finances.application.usecases.security.DefaultCreateAuthenticateUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.authenticate.SecurityGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateUseCaseTest {

    @InjectMocks
    private DefaultCreateAuthenticateUseCase useCase;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private SecurityGateway securityGateway;

    @Test
    public void givenAValidParams_whenCallsAuthenticate_shouldReturnToken() {
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "123456789";
        final var expectedToken = "authToken";

        final var aAccount = Account.newAccount("kaua", expectedEmail, expectedPassword);

        when(accountGateway.findByEmail(any()))
                .thenReturn(Optional.of(Account.with(aAccount)));

        when(securityGateway.generateToken(any()))
                .thenReturn(expectedToken);

        final var actualOutput = useCase.execute(expectedEmail, expectedPassword).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.token());
        Assertions.assertEquals(expectedToken, actualOutput.token());

        Mockito.verify(accountGateway, times(1)).findByEmail(expectedEmail);
        Mockito.verify(securityGateway, times(1)).generateToken(any());
    }

    @Test
    public void givenAnInvalidEmail_whenCallsAuthenticate_shouldReturnNotFoundException() {
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "123456789";
        final var expectedErrorMessage = "Account with ID kaua@mail.com was not found";

        final var actualOutput = useCase.execute(expectedEmail, expectedPassword).getLeft();

        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(accountGateway, times(1)).findByEmail(expectedEmail);
        Mockito.verify(securityGateway, times(0)).generateToken(any());
    }
}
