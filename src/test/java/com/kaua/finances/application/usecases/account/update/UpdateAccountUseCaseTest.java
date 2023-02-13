package com.kaua.finances.application.usecases.account.update;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountUseCaseTest {

    @InjectMocks
    private DefaultUpdateAccountUseCase useCase;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void givenAValidParams_whenCallsUpdateAccount_shouldReturnAccountId() {
        final var aAccount = Account.newAccount("kau", "kaua.@mail.com", "12345678");
        final var expectedId = aAccount.getId();

        final var expectedName = "kauã";
        final var expectedPassword = "12345678";

        when(accountGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Account.with(aAccount)));

        when(accountGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(
                expectedId,
                expectedName,
                expectedPassword
        ).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(accountGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(accountGateway, times(1)).update(argThat(accountUpdated ->
                Objects.equals(expectedName, accountUpdated.getName())
                        && Objects.equals(expectedPassword, accountUpdated.getPassword())
                        && Objects.equals(expectedId, accountUpdated.getId())
                        && Objects.nonNull(accountUpdated.getCreatedAt())
                        && accountUpdated.getUpdatedAt().isAfter(aAccount.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidParams_whenCallsUpdateAccount_shouldReturnDomainException() {
        final var aAccount = Account.newAccount("kau", "kaua.@mail.com", "12345678");
        final var expectedId = aAccount.getId();

        final String expectedName = null;
        final var expectedPassword = " ";

        final var expectedErrorMessageOne = "'name' should not be empty or null";
        final var expectedErrorMessageTwo = "'password' should not be empty or null";
        final var expectedErrorMessageThree = "'password' must contain 8 characters at least";

        when(accountGateway.findById(any()))
                .thenReturn(Optional.of(Account.with(aAccount)));

        final var actualException = useCase.execute(expectedId, expectedName, expectedPassword).getLeft();

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorMessageThree, actualException.getErrors().get(2).message());

        Mockito.verify(accountGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(accountGateway, times(0)).update(any());
    }
}
