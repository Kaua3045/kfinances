package com.kaua.finances.application.usecases.bill.update;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillCacheGateway;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateBillUseCaseTest {

    @InjectMocks
    private DefaultUpdateBillUseCase useCase;

    @Mock
    private BillGateway billGateway;

    @Mock
    private BillCacheGateway billCacheGateway;

    @Test
    public void givenAValidParams_whenCallsUpdateBill_shouldReturnBillId() {
        final var aAccount = Account.newAccount("kau", "kaua.@mail.com", "12345678");

        final var expectedTitle = "fatura 01";
        final String expectedDescription = null;
        final var expectedPending = true;

        final var aBill = Bill.newBill(
                aAccount,
                expectedTitle,
                expectedDescription,
                expectedPending
        );

        final var expectedId = aBill.getId();

        when(billGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Bill.with(aBill)));

        when(billGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(billGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(billGateway, times(1)).update(argThat(billUpdated ->
                Objects.equals(expectedTitle, billUpdated.getTitle())
                        && Objects.equals(expectedDescription, billUpdated.getDescription())
                        && Objects.equals(expectedPending, billUpdated.isPending())
                        && Objects.equals(expectedId, billUpdated.getId())
                        && Objects.nonNull(billUpdated.getCreatedAt())
                        && billUpdated.getUpdatedAt().isAfter(aBill.getUpdatedAt())
                        && Objects.isNull(billUpdated.getFinishedDate())
        ));
    }

    @Test
    public void givenAnInvalidParams_whenCallsUpdateBill_shouldReturnDomainException() {
        final var aAccount = new Account(
                null,
                "a",
                "a",
                "1234567890",
                null,
                null
        );

        final String expectedTitle = null;
        final var expectedDescription = GenerateRandomTextsUtils.generate3000Characters();
        final var expectedPending = true;

        final var expectedErrorMessageOne = "'accountId' should not be empty or null";
        final var expectedErrorMessageTwo = "'title' should not be empty or null";
        final var expectedErrorMessageThree = "'description' must be between 3000 characters";

        final var aBill = Bill.newBill(
                aAccount,
                expectedTitle,
                expectedDescription,
                expectedPending
        );

        final var expectedId = aBill.getId();

        when(billGateway.findById(any()))
                .thenReturn(Optional.of(Bill.with(aBill)));

        final var actualException = useCase.execute(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getLeft();

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorMessageThree, actualException.getErrors().get(2).message());

        Mockito.verify(billGateway, times(1)).findById(eq(expectedId));
        Mockito.verify(billGateway, times(0)).update(any());
    }

    @Test
    public void givenAnDisableBill_whenCallsUpdateBillAndEnableBill_shouldReturnBillId() {
        final var aAccount = Account.newAccount("kau", "kaua.@mail.com", "12345678");

        final var expectedTitle = "fatura 01";
        final String expectedDescription = null;
        final var expectedPending = true;

        final var aBill = Bill.newBill(
                aAccount,
                expectedTitle,
                expectedDescription,
                false
        );

        final var expectedId = aBill.getId();

        when(billGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Bill.with(aBill)));

        when(billGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertFalse(aBill.isPending());
        Assertions.assertNotNull(aBill.getFinishedDate());

        final var actualOutput = useCase.execute(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(billGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(billGateway, times(1)).update(argThat(billUpdated ->
                Objects.equals(expectedTitle, billUpdated.getTitle())
                        && Objects.equals(expectedDescription, billUpdated.getDescription())
                        && Objects.equals(expectedPending, billUpdated.isPending())
                        && Objects.equals(expectedId, billUpdated.getId())
                        && Objects.nonNull(billUpdated.getCreatedAt())
                        && billUpdated.getUpdatedAt().isAfter(aBill.getUpdatedAt())
                        && Objects.isNull(billUpdated.getFinishedDate())
        ));
    }

    @Test
    public void givenAnEnableBill_whenCallsUpdateBillAndDisableBill_shouldReturnBillId() {
        final var aAccount = Account.newAccount("kau", "kaua.@mail.com", "12345678");

        final var expectedTitle = "fatura 01";
        final String expectedDescription = null;
        final var expectedPending = false;

        final var aBill = Bill.newBill(
                aAccount,
                expectedTitle,
                expectedDescription,
                true
        );

        final var expectedId = aBill.getId();

        when(billGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Bill.with(aBill)));

        when(billGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aBill.isPending());
        Assertions.assertNull(aBill.getFinishedDate());

        final var actualOutput = useCase.execute(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(billGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(billGateway, times(1)).update(argThat(billUpdated ->
                Objects.equals(expectedTitle, billUpdated.getTitle())
                        && Objects.equals(expectedDescription, billUpdated.getDescription())
                        && Objects.equals(expectedPending, billUpdated.isPending())
                        && Objects.equals(expectedId, billUpdated.getId())
                        && Objects.nonNull(billUpdated.getCreatedAt())
                        && billUpdated.getUpdatedAt().isAfter(aBill.getUpdatedAt())
                        && Objects.nonNull(billUpdated.getFinishedDate())
        ));
    }
}
