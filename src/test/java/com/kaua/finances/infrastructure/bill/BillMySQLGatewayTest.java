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
}
