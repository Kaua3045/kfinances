package com.kaua.finances.application.usecases.bill.list;

import com.kaua.finances.application.usecases.bill.DefaultListBillByAccountIdUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountGateway;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillGateway;
import com.kaua.finances.domain.bills.BillListOutput;
import com.kaua.finances.domain.pagination.Pagination;
import com.kaua.finances.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListBillByAccountIdUseCaseTest {

    @InjectMocks
    private DefaultListBillByAccountIdUseCase useCase;

    @Mock
    private BillGateway billGateway;

    @Mock
    private AccountGateway accountGateway;

    @Test
    public void givenAValidParams_whenCallsListBill_shouldReturnBills() {
        final var aAccount = Account.newAccount("a", "a", "123456789");

        final var bills = List.of(
                Bill.newBill(aAccount, "fatura 01", null, true),
                Bill.newBill(aAccount, "fatura 02", null, false)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = bills.stream()
                .map(BillListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                bills
        );

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        when(accountGateway.findById(aAccount.getId()))
                .thenReturn(Optional.of(Account.with(aAccount)));

        when(billGateway.findAllByAccountId(aAccount.getId(), aQuery))
                .thenReturn(expectedPagination);

        final var actualOutput = useCase.execute(aAccount.getId(), aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(billGateway, times(1)).findAllByAccountId(aAccount.getId(), aQuery);
    }

    @Test
    public void givenAValidParams_whenCallsListBillAndResultIsEmpty_shouldReturnBills() {
        final var aAccount = Account.newAccount("a", "a", "123456789");

        final var bills = List.<Bill>of();
        final var expectedAccountId = aAccount.getId();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<Bill>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                bills
        );

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        when(accountGateway.findById(expectedAccountId))
                .thenReturn(Optional.of(Account.with(aAccount)));

        when(billGateway.findAllByAccountId(expectedAccountId, aQuery))
                .thenReturn(expectedPagination);

        final var actualOutput = useCase.execute(expectedAccountId, aQuery);

        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(billGateway, times(1)).findAllByAccountId(expectedAccountId, aQuery);
    }
}
