package com.kaua.finances.application.usecases.bill.delete;

import com.kaua.finances.application.usecases.bill.DefaultDeleteBillByIdUseCase;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteBillByIdUseCaseTest {

    @InjectMocks
    private DefaultDeleteBillByIdUseCase useCase;

    @Mock
    private BillGateway billGateway;

    @Test
    public void givenAValidId_whenCallsDeleteBill_shouldBeOk() {
        final var aAccount = Account.newAccount("a", "a", "1234567890");

        final var aBill = Bill.newBill(
                aAccount,
                "fatura",
                null,
                true
        );

        final var expectedId = aBill.getId();

        doNothing()
                .when(billGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        Mockito.verify(billGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteBill_shouldBeOk() {
        final var expectedId = "123";

        doNothing()
                .when(billGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        Mockito.verify(billGateway, times(1)).deleteById(expectedId);
    }
}
