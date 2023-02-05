package com.kaua.finances.application.usecases.account.create;

import com.kaua.finances.application.usecases.DefaultCreateAccountUseCase;
import com.kaua.finances.domain.account.AccountGateway;
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
}
