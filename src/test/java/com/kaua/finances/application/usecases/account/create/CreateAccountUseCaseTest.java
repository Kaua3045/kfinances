package com.kaua.finances.application.usecases.account.create;

import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.account.AccountRedisGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAccountUseCaseTest {

    @InjectMocks
    private DefaultCreateAccountUseCase useCase;

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private AccountRedisGateway accountRedisGateway;


    @Test
    public void givenAValidParams_whenCallsCreateAccount_shouldReturnAccountId() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";

        when(accountGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(expectedName, expectedEmail, expectedPassword).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(accountGateway, times(1)).create(argThat(aAccount ->
                Objects.equals(expectedName, aAccount.getName())
                && Objects.equals(expectedEmail, aAccount.getEmail())
                && Objects.equals(expectedPassword, aAccount.getPassword())
                && Objects.nonNull(aAccount.getId())
                && Objects.nonNull(aAccount.getCreatedAt())
                && Objects.nonNull(aAccount.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidParams_whenCallsCreateAccount_thenShouldReturnDomainException() {
        final var expectedName = " ";
        final String expectedEmail = null;
        final var expectedPassword = " ";

        final var expectedErrorMessageOne = "'name' should not be empty or null";
        final var expectedErrorMessageTwo = "'password' should not be empty or null";
        final var expectedErrorMessageThree = "'password' must contain 8 characters at least";
        final var expectedErrorMessageFour = "'email' should not be empty or null";

        final var actualException = useCase.execute(expectedName, expectedEmail, expectedPassword).getLeft();

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorMessageThree, actualException.getErrors().get(2).message());
        Assertions.assertEquals(expectedErrorMessageFour, actualException.getErrors().get(3).message());

        Mockito.verify(accountGateway, times(0)).create(any());
    }
}
