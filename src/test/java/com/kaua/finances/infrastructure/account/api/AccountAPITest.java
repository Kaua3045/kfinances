package com.kaua.finances.infrastructure.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.DomainException;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.account.CreateAccountUseCase;
import com.kaua.finances.application.usecases.account.DeleteAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.GetAccountByIdUseCase;
import com.kaua.finances.application.usecases.account.UpdateAccountUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.domain.account.AccountOutput;
import com.kaua.finances.domain.account.CreateAccountOutput;
import com.kaua.finances.domain.account.UpdateAccountOutput;
import com.kaua.finances.domain.validate.Error;
import com.kaua.finances.infrastructure.account.models.CreateAccountRequest;
import com.kaua.finances.infrastructure.api.AccountAPI;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test-integration")
@WebMvcTest(controllers = AccountAPI.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateAccountUseCase createAccountUseCase;

    @MockBean
    private UpdateAccountUseCase updateAccountUseCase;

    @MockBean
    private GetAccountByIdUseCase getAccountByIdUseCase;

    @MockBean
    private DeleteAccountByIdUseCase deleteAccountByIdUseCase;

    @Test
    public void givenAValidParams_whenCallsCreateAccount_shouldReturnAccountId() throws Exception {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";

        final var input = new CreateAccountRequest(expectedName, expectedEmail, expectedPassword);

        when(createAccountUseCase.execute(any(), any(), any()))
                .thenReturn(Either.right(CreateAccountOutput.from("123")));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo("123")));
    }

    @Test
    public void givenAnInvalidParams_whenCallsCreateAccount_shouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedEmail = " ";
        final var expectedPassword = " ";

        final var expectedErrorsMessage = List.of(
                new Error("'name' should not be empty or null"),
                new Error("'password' should not be empty or null"),
                new Error("'password' must contain 8 characters at least"),
                new Error("'email' should not be empty or null")
        );
        final var expectedErrorOne = expectedErrorsMessage.get(0).message();
        final var expectedErrorTwo = expectedErrorsMessage.get(1).message();
        final var expectedErrorThree = expectedErrorsMessage.get(2).message();
        final var expectedErrorFour = expectedErrorsMessage.get(3).message();

        final var input = new CreateAccountRequest(expectedName, expectedEmail, expectedPassword);

        when(createAccountUseCase.execute(any(), any(), any()))
                .thenReturn(Either.left(DomainException.with(expectedErrorsMessage)));

        final var request = MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors[0].message").value(expectedErrorOne))
                .andExpect(jsonPath("$.errors[1].message").value(expectedErrorTwo))
                .andExpect(jsonPath("$.errors[2].message").value(expectedErrorThree))
                .andExpect(jsonPath("$.errors[3].message").value(expectedErrorFour));
    }

    @Test
    public void givenAValidParams_whenCallsUpdateAccount_shouldReturnAccountId() throws Exception {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";
        final var expectedId = "123";

        when(updateAccountUseCase.execute(any(), any(), any(), any()))
                .thenReturn(Either.right(UpdateAccountOutput.from(expectedId)));

        final var input = new CreateAccountRequest(expectedName, expectedEmail, expectedPassword);

        final var request = MockMvcRequestBuilders.put("/accounts/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)));
    }

    @Test
    public void givenAnInvalidParams_whenCallsUpdateAccount_shouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedEmail = " ";
        final var expectedPassword = " ";
        final var expectedId = "123";

        final var expectedErrorsMessage = List.of(
                new Error("'name' should not be empty or null"),
                new Error("'password' should not be empty or null"),
                new Error("'password' must contain 8 characters at least"),
                new Error("'email' should not be empty or null")
        );
        final var expectedErrorOne = expectedErrorsMessage.get(0).message();
        final var expectedErrorTwo = expectedErrorsMessage.get(1).message();
        final var expectedErrorThree = expectedErrorsMessage.get(2).message();
        final var expectedErrorFour = expectedErrorsMessage.get(3).message();

        final var input = new CreateAccountRequest(expectedName, expectedEmail, expectedPassword);

        when(updateAccountUseCase.execute(any(), any(), any(), any()))
                .thenReturn(Either.left(DomainException.with(expectedErrorsMessage)));

        final var request = MockMvcRequestBuilders.put("/accounts/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors[0].message").value(expectedErrorOne))
                .andExpect(jsonPath("$.errors[1].message").value(expectedErrorTwo))
                .andExpect(jsonPath("$.errors[2].message").value(expectedErrorThree))
                .andExpect(jsonPath("$.errors[3].message").value(expectedErrorFour));
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnAccount() throws Exception {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);
        final var expectedId = aAccount.getId();

        when(getAccountByIdUseCase.execute(any()))
                .thenReturn(AccountOutput.from(aAccount));

        final var request = MockMvcRequestBuilders.get("/accounts/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.email", equalTo(expectedEmail)))
                .andExpect(jsonPath("$.bills", equalTo(Collections.emptyList())));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetById_shouldReturnNotFoundException() throws Exception {
        final var expectedErrorMessage = "Account with ID 123 was not found";
        final var expectedId = "123";

        when(getAccountByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Account.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/accounts/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldReturnOk() throws Exception {
        final var expectedId = "123";

        doNothing()
                .when(deleteAccountByIdUseCase).execute(expectedId);

        final var request = MockMvcRequestBuilders.delete("/accounts/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}
