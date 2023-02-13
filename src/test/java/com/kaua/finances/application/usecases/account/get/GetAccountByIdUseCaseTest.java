package com.kaua.finances.application.usecases.account.get;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.account.retrieve.DefaultGetAccountByIdUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAccountByIdUseCaseTest {

    @InjectMocks
    private DefaultGetAccountByIdUseCase useCase;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnAccount() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";
        final var expectedBills = List.<String>of();

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);

        final var expectedId = aAccount.getId();

        when(accountGateway.findById(any()))
                .thenReturn(Optional.of(Account.with(aAccount)));

        final var actualOutput = useCase.execute(expectedId);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedEmail, actualOutput.email());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnNotFoundException() {
        final var expectedId = "123";
        final var expectedErrorMessage = "Account with ID 123 was not found";

        when(accountGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
