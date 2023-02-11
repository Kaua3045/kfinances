package com.kaua.finances.infrastructure.bill;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.infrastructure.MySQLGatewayTest;
import com.kaua.finances.infrastructure.account.AccountMySQLGateway;
import com.kaua.finances.infrastructure.bill.persistence.BillJpaFactory;
import com.kaua.finances.infrastructure.bill.persistence.BillRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class BillMySQLGatewayTest {

    @Autowired
    private BillMySQLGateway billGateway;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AccountMySQLGateway accountGateway;

    @Test
    public void givenAValidParams_whenCallsCreate_shouldReturnANewBill() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = null;
        final var expectedPending = true;

        final var aAccount = Account.newAccount("kaua", "kaua@mail.com", "12345678");

        accountGateway.create(aAccount);

        final var aBill = Bill.newBill(aAccount, expectedTitle, expectedDescription, expectedPending);

        Assertions.assertEquals(0, billRepository.count());

        final var actualBill = billGateway.create(aBill);

        Assertions.assertEquals(1, billRepository.count());

        Assertions.assertEquals(aBill.getId(), actualBill.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualBill.getAccountId().getId());
        Assertions.assertEquals(expectedTitle, actualBill.getTitle());
        Assertions.assertEquals(expectedDescription, actualBill.getDescription());
        Assertions.assertEquals(expectedPending, actualBill.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualBill.getCreatedAt());
        Assertions.assertEquals(aBill.getUpdatedAt(), actualBill.getUpdatedAt());
        Assertions.assertEquals(aBill.getFinishedDate(), actualBill.getFinishedDate());

        final var actualEntity = billRepository.findById(aBill.getId()).get();

        Assertions.assertEquals(aBill.getId(), actualEntity.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualEntity.getAccount().getId());
        Assertions.assertEquals(expectedTitle, actualEntity.getTitle());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedPending, actualEntity.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aBill.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aBill.getFinishedDate(), actualEntity.getFinishedDate());
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnBill() {
        final var aAccount = Account.newAccount("kaua", "kaua2@mail.com", "12345678");
        accountGateway.create(aAccount);

        final var expectedTitle = "fatura 01";
        final var expectedDescription = "fatura do cartao";
        final var expectedPending = true;

        final var aBill = Bill.newBill(
                aAccount,
                expectedTitle,
                expectedDescription,
                expectedPending
        );

        Assertions.assertEquals(0, billRepository.count());

        billRepository.saveAndFlush(BillJpaFactory.from(aBill));

        Assertions.assertEquals(1, billRepository.count());

        final var actualBill = billGateway.findById(aBill.getId()).get();

        Assertions.assertEquals(aBill.getId(), actualBill.getId());
        Assertions.assertEquals(expectedTitle, actualBill.getTitle());
        Assertions.assertEquals(expectedDescription, actualBill.getDescription());
        Assertions.assertEquals(expectedPending, actualBill.isPending());
        Assertions.assertEquals(aAccount.getId(), actualBill.getAccountId().getId());
        Assertions.assertEquals(aBill.getCreatedAt(), actualBill.getCreatedAt());
        Assertions.assertEquals(aBill.getUpdatedAt(), actualBill.getUpdatedAt());
        Assertions.assertNull(actualBill.getFinishedDate());
    }

    @Test
    public void givenAValidParams_whenCallsUpdate_shouldReturnBillId() {
        final var aAccount = Account.newAccount("kaua", "kaua4@mail.com", "12345678");
        accountGateway.create(aAccount);

        final var expectedTitle = "fatura 01";
        final var expectedDescription = "fatura do cartao";
        final var expectedPending = true;

        final var aBill = Bill.newBill(
                aAccount,
                "fat",
                null,
                expectedPending
        );

        Assertions.assertDoesNotThrow(aBill::validate);
        Assertions.assertEquals(0, billRepository.count());

        billRepository.saveAndFlush(BillJpaFactory.from(aBill));

        Assertions.assertEquals(1, billRepository.count());

        final var actualBillEntity = billRepository.findById(aBill.getId()).get();

        Assertions.assertEquals("fat", actualBillEntity.getTitle());
        Assertions.assertNull(actualBillEntity.getDescription());
        Assertions.assertEquals(expectedPending, actualBillEntity.isPending());

        final var aUpdatedBill = Bill.with(aBill).update(
                expectedTitle,
                expectedDescription,
                expectedPending
        );

        final var actualOutput = billGateway.update(aUpdatedBill);

        Assertions.assertEquals(1, billRepository.count());

        Assertions.assertEquals(aBill.getId(), actualOutput.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualOutput.getAccountId().getId());
        Assertions.assertEquals(expectedTitle, actualOutput.getTitle());
        Assertions.assertEquals(expectedDescription, actualOutput.getDescription());
        Assertions.assertEquals(expectedPending, actualOutput.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualOutput.getCreatedAt());
        Assertions.assertTrue(actualOutput.getUpdatedAt().isAfter(aBill.getUpdatedAt()));
        Assertions.assertNull(actualOutput.getFinishedDate());

        final var actualEntity = billRepository.findById(aBill.getId()).get();

        Assertions.assertEquals(aBill.getId(), actualEntity.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualEntity.getAccount().getId());
        Assertions.assertEquals(expectedTitle, actualEntity.getTitle());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedPending, actualEntity.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(actualEntity.getUpdatedAt().isAfter(aBill.getUpdatedAt()));
        Assertions.assertNull(actualEntity.getFinishedDate());
    }

    @Test
    public void givenAValidDisableBill_whenCallsUpdateAndEnableBill_shouldReturnBillId() {
        final var aAccount = Account.newAccount("kaua", "kaua3@mail.com", "12345678");
        accountGateway.create(aAccount);

        final var expectedTitle = "fatura 01";
        final var expectedDescription = "fatura do cartao";
        final var expectedPending = true;

        final var aBill = Bill.newBill(
                aAccount,
                "fat",
                null,
                false
        );

        Assertions.assertDoesNotThrow(aBill::validate);
        Assertions.assertEquals(0, billRepository.count());

        billRepository.saveAndFlush(BillJpaFactory.from(aBill));

        Assertions.assertEquals(1, billRepository.count());

        final var actualBillEntity = billRepository.findById(aBill.getId()).get();

        Assertions.assertEquals("fat", actualBillEntity.getTitle());
        Assertions.assertNull(actualBillEntity.getDescription());
        Assertions.assertFalse(aBill.isPending());
        Assertions.assertNotNull(aBill.getFinishedDate());

        final var aUpdatedBill = Bill.with(aBill).update(
                expectedTitle,
                expectedDescription,
                expectedPending
        );

        final var actualOutput = billGateway.update(aUpdatedBill);

        Assertions.assertEquals(1, billRepository.count());

        Assertions.assertEquals(aBill.getId(), actualOutput.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualOutput.getAccountId().getId());
        Assertions.assertEquals(expectedTitle, actualOutput.getTitle());
        Assertions.assertEquals(expectedDescription, actualOutput.getDescription());
        Assertions.assertEquals(expectedPending, actualOutput.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualOutput.getCreatedAt());
        Assertions.assertTrue(actualOutput.getUpdatedAt().isAfter(aBill.getUpdatedAt()));
        Assertions.assertNull(actualOutput.getFinishedDate());

        final var actualEntity = billRepository.findById(aBill.getId()).get();

        Assertions.assertEquals(aBill.getId(), actualEntity.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualEntity.getAccount().getId());
        Assertions.assertEquals(expectedTitle, actualEntity.getTitle());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedPending, actualEntity.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(actualEntity.getUpdatedAt().isAfter(aBill.getUpdatedAt()));
        Assertions.assertNull(actualEntity.getFinishedDate());
    }

    @Test
    public void givenAValidEnableBill_whenCallsUpdateAndDisableBill_shouldReturnBillId() {
        final var aAccount = Account.newAccount("kaua", "kaua3@mail.com", "12345678");
        accountGateway.create(aAccount);

        final var expectedTitle = "fatura 01";
        final var expectedDescription = "fatura do cartao";
        final var expectedPending = false;

        final var aBill = Bill.newBill(
                aAccount,
                "fat",
                null,
                true
        );

        Assertions.assertDoesNotThrow(aBill::validate);
        Assertions.assertEquals(0, billRepository.count());

        billRepository.saveAndFlush(BillJpaFactory.from(aBill));

        Assertions.assertEquals(1, billRepository.count());

        final var actualBillEntity = billRepository.findById(aBill.getId()).get();

        Assertions.assertEquals("fat", actualBillEntity.getTitle());
        Assertions.assertNull(actualBillEntity.getDescription());
        Assertions.assertTrue(aBill.isPending());
        Assertions.assertNull(aBill.getFinishedDate());

        final var aUpdatedBill = Bill.with(aBill).update(
                expectedTitle,
                expectedDescription,
                expectedPending
        );

        final var actualOutput = billGateway.update(aUpdatedBill);

        Assertions.assertEquals(1, billRepository.count());

        Assertions.assertEquals(aBill.getId(), actualOutput.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualOutput.getAccountId().getId());
        Assertions.assertEquals(expectedTitle, actualOutput.getTitle());
        Assertions.assertEquals(expectedDescription, actualOutput.getDescription());
        Assertions.assertEquals(expectedPending, actualOutput.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualOutput.getCreatedAt());
        Assertions.assertTrue(actualOutput.getUpdatedAt().isAfter(aBill.getUpdatedAt()));
        Assertions.assertNotNull(actualOutput.getFinishedDate());

        final var actualEntity = billRepository.findById(aBill.getId()).get();

        Assertions.assertEquals(aBill.getId(), actualEntity.getId());
        Assertions.assertEquals(aBill.getAccountId().getId(), actualEntity.getAccount().getId());
        Assertions.assertEquals(expectedTitle, actualEntity.getTitle());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedPending, actualEntity.isPending());
        Assertions.assertEquals(aBill.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(actualEntity.getUpdatedAt().isAfter(aBill.getUpdatedAt()));
        Assertions.assertNotNull(actualEntity.getFinishedDate());
    }
}
