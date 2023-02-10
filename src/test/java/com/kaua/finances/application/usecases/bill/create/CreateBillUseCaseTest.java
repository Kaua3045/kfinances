package com.kaua.finances.application.usecases.bill.create;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.bill.DefaultCreateBillUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.domain.utils.GenerateRandomTextsUtils;
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
public class CreateBillUseCaseTest {

    @InjectMocks
    private DefaultCreateBillUseCase useCase;

    @Mock
    private BillGateway billGateway;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void givenAValidParams_whenCallsCreateBill_shouldReturnBillId() {
        final var expectedAccount = Account.newAccount(
                "kaua",
                "kaua@mail.com",
                "12345678"
        );

        final var expectedTitle = "fatura 01";
        final String expectedDescription = null;
        final var expectedPending = true;

        when(accountGateway.findById(eq(expectedAccount.getId())))
                .thenReturn(Optional.of(Account.with(expectedAccount)));

        when(billGateway.create(any()))
                .thenAnswer(returnsFirstArg());


        final var actualOutput = useCase.execute(
                expectedAccount.getId(),
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(billGateway, times(1)).create(argThat(bill ->
                Objects.nonNull(bill.getId())
                && Objects.equals(expectedAccount.getId(), bill.getAccountId().getId())
                && Objects.equals(expectedTitle, bill.getTitle())
                && Objects.equals(expectedDescription, bill.getDescription())
                && Objects.equals(expectedPending, bill.isPending())
                && Objects.nonNull(bill.getCreatedAt())
                && Objects.nonNull(bill.getUpdatedAt())
                && Objects.isNull(bill.getFinishedDate())
        ));
    }

    @Test
    public void givenAnInvalidParams_whenCallsCreateBill_shouldReturnDomainException() {
        final var expectedAccount = new Account(
                null,
                "kaua",
                "kaua@mail.com",
                "123456789",
                null,
                null
        );

        final var expectedTitle = " ";
        final String expectedDescription = GenerateRandomTextsUtils.generate3000Characters();
        final var expectedPending = true;

        final var expectedErrorMessageOne = "'accountId' should not be empty or null";
        final var expectedErrorMessageTwo = "'title' should not be empty or null";
        final var expectedErrorMessageThree = "'description' must be between 3000 characters";

        when(accountGateway.findById(any()))
                .thenReturn(Optional.of(Account.with(expectedAccount)));

        final var actualException = useCase.execute(
                expectedAccount.getId(),
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getLeft();

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorMessageThree, actualException.getErrors().get(2).message());
    }

    @Test
    public void givenAnInvalidAccountId_whenCallsCreateBill_shouldReturnNotFoundException() {
        final var expectedAccount = new Account(
                null,
                "kaua",
                "kaua@mail.com",
                "123456789",
                null,
                null
        );

        final var expectedTitle = "a";
        final String expectedDescription = null;
        final var expectedPending = true;

        final var expectedErrorMessageOne = "Account with ID null was not found";

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(
                        expectedAccount.getId(),
                        expectedTitle,
                        expectedDescription,
                        expectedPending
                ).getLeft());

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getMessage());
    }
}
