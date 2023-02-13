package com.kaua.finances.application.usecases.bill.get;

import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.bill.retrieve.get.DefaultGetBillByIdUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetBillByIdUseCaseTest {

    @InjectMocks
    private DefaultGetBillByIdUseCase useCase;

    @Mock
    private BillGateway billGateway;

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnBill() {
        final var aAccount = Account.newAccount("kaua", "kaua@mail.com", "12345678");

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

        when(billGateway.findById(any()))
                .thenReturn(Optional.of(Bill.with(aBill)));

        final var actualOutput = useCase.execute(expectedId);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(expectedTitle, actualOutput.title());
        Assertions.assertEquals(expectedDescription, actualOutput.description());
        Assertions.assertEquals(expectedPending, actualOutput.pending());
        Assertions.assertEquals(aAccount.getId(), actualOutput.accountId());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnNotFoundException() {
        final var expectedId = "123";
        final var expectedErrorMessage = "Bill with ID 123 was not found";

        when(billGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
