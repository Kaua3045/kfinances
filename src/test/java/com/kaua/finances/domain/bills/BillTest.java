package com.kaua.finances.domain.bills;

import com.kaua.finances.domain.utils.GenerateRandomTextsUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BillTest {

    @Test
    public void givenAValidParams_whenCallsNewBill_thenInstantiateBill() {
        final var expectedTitle = "fatura 01";
        final var expectedDescription = "fatura do cartão de credito";
        final var expectedPending = true;

        final var aBill = Bill.newBill(expectedTitle, expectedDescription, expectedPending);

        Assertions.assertNotNull(aBill);
        Assertions.assertNotNull(aBill.getId());
        Assertions.assertEquals(expectedTitle, aBill.getTitle());
        Assertions.assertEquals(expectedDescription, aBill.getDescription());
        Assertions.assertEquals(expectedPending, aBill.isPending());
        Assertions.assertNotNull(aBill.getCreatedAt());
        Assertions.assertNotNull(aBill.getUpdatedAt());
        Assertions.assertNull(aBill.getFinishedDate());
    }

    @Test
    public void givenAnInvalidTitle_whenCallsNewBill_thenInstantiateBill() {
        final var expectedTitle = " ";
        final String expectedDescription = null;
        final var expectedPending = true;
        final var expectedErrorMessage = "'title' should not be empty or null";

        final var aBill = Bill.newBill(expectedTitle, expectedDescription, expectedPending);

        final var actualException = aBill.validate();

        Assertions.assertEquals(expectedErrorMessage, actualException.get(0).message());
    }

    @Test
    public void givenAnInvalidDescriptionLengthMoreThan3000_whenCallsNewBill_thenInstantiateBill() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = GenerateRandomTextsUtils.generate3000Characters();
        final var expectedPending = true;
        final var expectedErrorMessage = "'description' must be between 3000 characters";

        final var aBill = Bill.newBill(expectedTitle, expectedDescription, expectedPending);

        final var actualException = aBill.validate();

        Assertions.assertEquals(expectedErrorMessage, actualException.get(0).message());
    }

    @Test
    public void givenAValidBill_whenCallsUpdate_thenReturnBillUpdated() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = "fatura do cartão de credito";
        final var expectedPending = true;

        final var aBill = Bill.newBill("fa01", null, true);

        Assertions.assertDoesNotThrow(aBill::validate);

        final var aUpdatedAtABill = aBill.getUpdatedAt();

        final var actualBill = aBill.update(expectedTitle, expectedDescription, expectedPending);

        Assertions.assertDoesNotThrow(actualBill::validate);

        Assertions.assertEquals(aBill.getId(), actualBill.getId());
        Assertions.assertEquals(expectedTitle, actualBill.getTitle());
        Assertions.assertEquals(expectedDescription, actualBill.getDescription());
        Assertions.assertEquals(expectedPending, actualBill.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualBill.getCreatedAt());
        Assertions.assertTrue(actualBill.getUpdatedAt().isAfter(aUpdatedAtABill));
        Assertions.assertNull(actualBill.getFinishedDate());
    }

    @Test
    public void givenAValidDisableBill_whenCallsUpdateAndEnable_thenReturnBillUpdated() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = "fatura do cartão de credito";
        final var expectedPending = true;

        final var aBill = Bill.newBill("fa01", null, false);

        Assertions.assertDoesNotThrow(aBill::validate);

        final var aUpdatedAtABill = aBill.getUpdatedAt();

        final var actualBill = aBill.update(expectedTitle, expectedDescription, expectedPending);

        Assertions.assertDoesNotThrow(actualBill::validate);

        Assertions.assertEquals(aBill.getId(), actualBill.getId());
        Assertions.assertEquals(expectedTitle, actualBill.getTitle());
        Assertions.assertEquals(expectedDescription, actualBill.getDescription());
        Assertions.assertTrue(aBill.isPending());
        Assertions.assertEquals(expectedPending, actualBill.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualBill.getCreatedAt());
        Assertions.assertTrue(actualBill.getUpdatedAt().isAfter(aUpdatedAtABill));
        Assertions.assertNull(actualBill.getFinishedDate());
    }

    @Test
    public void givenAValidEnableBill_whenCallsUpdateAndDisable_thenReturnBillUpdated() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = "fatura do cartão de credito";
        final var expectedPending = false;

        final var aBill = Bill.newBill("fa01", null, true);

        Assertions.assertDoesNotThrow(aBill::validate);

        final var aUpdatedAtABill = aBill.getUpdatedAt();

        final var actualBill = aBill.update(expectedTitle, expectedDescription, expectedPending);

        Assertions.assertDoesNotThrow(actualBill::validate);

        Assertions.assertEquals(aBill.getId(), actualBill.getId());
        Assertions.assertEquals(expectedTitle, actualBill.getTitle());
        Assertions.assertEquals(expectedDescription, actualBill.getDescription());
        Assertions.assertFalse(aBill.isPending());
        Assertions.assertEquals(expectedPending, actualBill.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualBill.getCreatedAt());
        Assertions.assertTrue(actualBill.getUpdatedAt().isAfter(aUpdatedAtABill));
        Assertions.assertNotNull(actualBill.getFinishedDate());
    }

    @Test
    public void givenADisableBill_whenCallsEnableBill_shouldReturnEnabledBill() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = "fatura do cartão de credito";
        final var expectedPending = true;

        final var aBill = Bill.newBill(expectedTitle, expectedDescription, false);

        Assertions.assertDoesNotThrow(aBill::validate);

        final var aBillUpdatedAt = aBill.getUpdatedAt();

        final var actualBill = aBill.enableBill();

        Assertions.assertEquals(aBill.getId(), actualBill.getId());
        Assertions.assertEquals(expectedTitle, actualBill.getTitle());
        Assertions.assertEquals(expectedDescription, actualBill.getDescription());
        Assertions.assertTrue(aBill.isPending());
        Assertions.assertEquals(expectedPending, actualBill.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualBill.getCreatedAt());
        Assertions.assertTrue(actualBill.getUpdatedAt().isAfter(aBillUpdatedAt));
        Assertions.assertNull(actualBill.getFinishedDate());
    }

    @Test
    public void givenAEnableBill_whenCallsDisableBill_shouldReturnDisabledBill() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = "fatura do cartão de credito";
        final var expectedPending = false;

        final var aBill = Bill.newBill(expectedTitle, expectedDescription, true);

        Assertions.assertDoesNotThrow(aBill::validate);

        final var aBillUpdatedAt = aBill.getUpdatedAt();

        final var actualBill = aBill.disableBill();

        Assertions.assertEquals(aBill.getId(), actualBill.getId());
        Assertions.assertEquals(expectedTitle, actualBill.getTitle());
        Assertions.assertEquals(expectedDescription, actualBill.getDescription());
        Assertions.assertFalse(aBill.isPending());
        Assertions.assertEquals(expectedPending, actualBill.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualBill.getCreatedAt());
        Assertions.assertTrue(actualBill.getUpdatedAt().isAfter(aBillUpdatedAt));
        Assertions.assertNotNull(actualBill.getFinishedDate());
    }
}
