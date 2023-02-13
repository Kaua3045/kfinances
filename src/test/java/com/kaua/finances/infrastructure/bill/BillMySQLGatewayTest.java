package com.kaua.finances.infrastructure.bill;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.pagination.SearchQuery;
import com.kaua.finances.infrastructure.MySQLGatewayTest;
import com.kaua.finances.infrastructure.account.AccountMySQLGateway;
import com.kaua.finances.infrastructure.account.persistence.AccountJpaFactory;
import com.kaua.finances.infrastructure.account.persistence.AccountRepository;
import com.kaua.finances.infrastructure.bill.persistence.BillJpaFactory;
import com.kaua.finances.infrastructure.bill.persistence.BillRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class BillMySQLGatewayTest {

    @Autowired
    private BillMySQLGateway billGateway;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AccountMySQLGateway accountGateway;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        final var aAccount = Account.newAccount("kaua", "kaua@mail.com", "1234567890");
        accountRepository.findByEmail("kaua@mail.com").orElse(
                accountRepository.saveAndFlush(AccountJpaFactory.from(aAccount))
        );
    }


    @Test
    public void givenAValidParams_whenCallsCreate_shouldReturnANewBill() {
        final var expectedTitle = "fatura 01";
        final String expectedDescription = null;
        final var expectedPending = true;

        final var aAccount = Account.newAccount("kaua", "kaua3@mail.com", "12345678");

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
        final var aAccount = Account.newAccount("kaua", "kaua5@mail.com", "12345678");
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

    @Test
    public void givenAValidId_whenCallsDeleteBill_shouldReturnOk() {
        final var aAccount = Account.newAccount("kaua", "kaua3@mail.com", "12345678");
        accountGateway.create(aAccount);

        final var aBill = Bill.newBill(aAccount, "fatura", null, true);

        Assertions.assertDoesNotThrow(aBill::validate);
        Assertions.assertEquals(0, billRepository.count());

        billRepository.saveAndFlush(BillJpaFactory.from(aBill));

        Assertions.assertEquals(1, billRepository.count());

        billGateway.deleteById(aBill.getId());

        Assertions.assertEquals(0, billRepository.count());
    }

    @Test
    public void givenEmptyBills_whenCallsFindAllByAccountId_shouldReturnEmptyList() {
        final var aAccount = Account.newAccount("kaua", "kaua3@mail.com", "12345678");
        accountGateway.create(aAccount);

        final var expectedAccountId = aAccount.getId();

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualPage = billGateway.findAllByAccountId(expectedAccountId, aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,4,4,Carro Financiamento",
            "title,desc,0,10,4,4,Mercado mês",
            "pending,asc,0,10,4,4,Financiamento casa",
            "pending,desc,0,10,4,4,Carro Financiamento",
    })
    public void givenAValidSortAndDirection_whenCallsFindAllByAccountId_shouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedBillTitle
    ) {
        final var aAccount = AccountJpaFactory.toDomain(accountRepository.findByEmail("kaua@mail.com").get());

        makeBills(aAccount);
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualPage = billGateway.findAllByAccountId(aAccount.getId(), aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedBillTitle, actualPage.items().get(0).getTitle());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,4,Carro Financiamento;Debito mês",
            "1,2,2,4,Financiamento casa;Mercado mês"
    })
    public void givenAValidDefaultSortAndDirection_whenCallsFindAllByAccountId_shouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedBills
    ) {
        final var aAccount = AccountJpaFactory.toDomain(accountRepository.findByEmail("kaua@mail.com").get());

        makeBills(aAccount);
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var actualPage = billGateway.findAllByAccountId(aAccount.getId(), aQuery);

        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedTitle : expectedBills.split(";")) {
            final var actualTitle = actualPage.items().get(index).getTitle();
            Assertions.assertEquals(expectedTitle, actualTitle);
            index++;
        }
    }

    private void makeBills(final Account aAccount) {
        billRepository.saveAllAndFlush(List.of(
                BillJpaFactory.from(Bill.newBill(aAccount, "Carro Financiamento", null, true)),
                BillJpaFactory.from(Bill.newBill(aAccount, "Mercado mês", null, true)),
                BillJpaFactory.from(Bill.newBill(aAccount, "Debito mês", null, true)),
                BillJpaFactory.from(Bill.newBill(aAccount, "Financiamento casa", null, false))
        ));
    }
}
