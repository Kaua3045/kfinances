package com.kaua.finances.infrastructure.bill.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.bill.CreateBillUseCase;
import com.kaua.finances.application.usecases.bill.GetBillByIdUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountOutput;
import com.kaua.finances.domain.bills.Bill;
import com.kaua.finances.domain.bills.BillOutput;
import com.kaua.finances.domain.bills.CreateBillOutput;
import com.kaua.finances.domain.utils.GenerateRandomTextsUtils;
import com.kaua.finances.domain.validate.Error;
import com.kaua.finances.infrastructure.account.models.CreateAccountRequest;
import com.kaua.finances.infrastructure.account.persistence.AccountJpaFactory;
import com.kaua.finances.infrastructure.account.persistence.AccountRepository;
import com.kaua.finances.infrastructure.api.BillAPI;
import com.kaua.finances.infrastructure.bill.models.CreateBillRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test-integration")
@WebMvcTest(controllers = BillAPI.class)
@AutoConfigureMockMvc(addFilters = false)
public class BillAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateBillUseCase createBillUseCase;

    @MockBean
    private GetBillByIdUseCase getBillByIdUseCase;

    @Test
    public void givenAValidParams_whenCallsCreateBill_shouldReturnBillId() throws Exception {
        final var aAccount = Account.newAccount("kaua", "kaua@mail.com", "123456789");

        final var expectedTitle = "fatura 01";
        final var expectedDescription = "fatura do cartão";
        final var expectedPending = true;

        final var input = new CreateBillRequest(
                expectedTitle,
                expectedDescription,
                expectedPending,
                aAccount.getId()
        );

        when(createBillUseCase.execute(
                input.accountId(),
                input.title(),
                input.description(),
                input.pending()
        )).thenReturn(Either.right(CreateBillOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo("123")));
    }

    @Test
    public void givenAnInvalidParams_whenCallsCreateBill_shouldReturnDomainException() throws Exception {
        final var aAccount = new Account(
                null,
                "a",
                "a",
                "123456789",
                null,
                null
        );

        final String expectedTitle = null;
        final var expectedDescription = GenerateRandomTextsUtils.generate3000Characters();
        final var expectedPending = true;

        final var expectedErrorsMessage = List.of(
                new Error("'accountId' should not be empty or null"),
                new Error("'title' should not be empty or null"),
                new Error("'description' must be between 3000 characters")
        );

        final var expectedErrorOne = expectedErrorsMessage.get(0).message();
        final var expectedErrorTwo = expectedErrorsMessage.get(1).message();
        final var expectedErrorThree = expectedErrorsMessage.get(2).message();

        final var input = new CreateBillRequest(
                expectedTitle,
                expectedDescription,
                expectedPending,
                aAccount.getId()
        );

        when(createBillUseCase.execute(
                input.accountId(),
                input.title(),
                input.description(),
                input.pending()
        )).thenReturn(Either.left(DomainException.with(expectedErrorsMessage)));

        final var request = MockMvcRequestBuilders.post("/bills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors[0].message").value(expectedErrorOne))
                .andExpect(jsonPath("$.errors[1].message").value(expectedErrorTwo))
                .andExpect(jsonPath("$.errors[2].message").value(expectedErrorThree));
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnBill() throws Exception {
        final var aAccount = Account.newAccount("kaua", "kaua@mail.com", "123456890");

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

        when(getBillByIdUseCase.execute(eq(aBill.getId())))
                .thenReturn(BillOutput.from(aBill));

        final var request = MockMvcRequestBuilders.get("/bills/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.pending", equalTo(expectedPending)))
                .andExpect(jsonPath("$.finishedDate", equalTo(aBill.getFinishedDate())))
                .andExpect(jsonPath("$.accountId", equalTo(aBill.getAccountId().getId())));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnNotFoundException() throws Exception {
        final var expectedErrorMessage = "Bill with ID 123 was not found";
        final var expectedId = "123";

        when(getBillByIdUseCase.execute(eq(expectedId)))
                .thenThrow(NotFoundException.with(Bill.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/bills/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }
}