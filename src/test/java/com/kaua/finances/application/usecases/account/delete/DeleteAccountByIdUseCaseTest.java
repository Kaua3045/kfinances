package com.kaua.finances.application.usecases.account.delete;

import com.kaua.finances.application.usecases.account.DefaultDeleteAccountByIdUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteAccountByIdUseCaseTest {

    @InjectMocks
    private DefaultDeleteAccountByIdUseCase useCase;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void givenAValidId_whenCallsDeleteAccount_shouldBeOk() {
        final var aAccount = Account.newAccount("kauã", "kaua@mail.com", "12345678");

        final var expectedId = aAccount.getId();

        doNothing()
                .when(accountGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        Mockito.verify(accountGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteAccount_shouldBeOk() {
        final var expectedId = "123";

        doNothing()
                .when(accountGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        Mockito.verify(accountGateway, times(1)).deleteById(eq(expectedId));
    }
}
