package com.kaua.finances.application.usecases.bill.update;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
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
public class UpdatePendingBillUseCaseTest {

    @InjectMocks
    private DefaultUpdatePendingBillUseCase useCase;

    @Mock
    private BillGateway billGateway;

    @Test
    public void givenAValidIdAndPendingToDisable_whenCallsUpdatePendingEnable_shouldReturnBillId() {
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

        Assertions.assertTrue(aBill.isPending());
        Assertions.assertNull(aBill.getFinishedDate());

        when(billGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Bill.with(aBill)));

        when(billGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(expectedId, expectedPending).getRight();

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

    @Test
    public void givenAValidIdAndPendingToEnable_whenCallsUpdatePendingDisable_shouldReturnBillId() {
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

        Assertions.assertFalse(aBill.isPending());
        Assertions.assertNotNull(aBill.getFinishedDate());

        when(billGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Bill.with(aBill)));

        when(billGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(expectedId, expectedPending).getRight();

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
    public void givenAnInvalidId_whenCallsUpdatePending_shouldReturnNotFoundException() {
        final var expectedId = "123";
        final var expectedErrorMessage = "Bill with ID 123 was not found";

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId, true)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
