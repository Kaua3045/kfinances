package com.kaua.finances.application.usecases.bill.create;

import com.kaua.finances.application.usecases.bill.DefaultCreateBillUseCase;
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

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateBillUseCaseTest {

    @InjectMocks
    private DefaultCreateBillUseCase useCase;

    @Mock
    private BillGateway billGateway;

    @Test
    public void givenAValidParams_whenCallsCreateBill_shouldReturnBillId() {
        final var expectedAccountId = "123";
        final var expectedTitle = "fatura 01";
        final String expectedDescription = null;
        final var expectedPending = true;

        when(billGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(
                expectedAccountId,
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getRight();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(billGateway, times(1)).create(argThat(bill ->
                Objects.nonNull(bill.getId())
                && Objects.equals(expectedAccountId, bill.getAccountId())
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
        final var expectedAccountId = " ";
        final var expectedTitle = " ";
        final String expectedDescription = GenerateRandomTextsUtils.generate3000Characters();
        final var expectedPending = true;

        final var expectedErrorMessageOne = "'accountId' should not be empty or null";
        final var expectedErrorMessageTwo = "'title' should not be empty or null";
        final var expectedErrorMessageThree = "'description' must be between 3000 characters";

        final var actualException = useCase.execute(
                expectedAccountId,
                expectedTitle,
                expectedDescription,
                expectedPending
        ).getLeft();

        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        Assertions.assertEquals(expectedErrorMessageThree, actualException.getErrors().get(2).message());
    }
}
