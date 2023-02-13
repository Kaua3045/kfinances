package com.kaua.finances.infrastructure.authenticate.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.finances.application.either.Either;
import com.kaua.finances.application.exceptions.NotFoundException;
import com.kaua.finances.application.usecases.security.CreateAuthenticateUseCase;
import com.kaua.finances.domain.account.Account;
import com.kaua.finances.application.usecases.security.output.AuthenticateTokenOutput;
import com.kaua.finances.infrastructure.account.models.AuthenticateRequest;
import com.kaua.finances.infrastructure.api.AuthAPI;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ActiveProfiles("test-integration")
@WebMvcTest(controllers = AuthAPI.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CreateAuthenticateUseCase createAuthenticateUseCase;

    @Test
    public void givenAValidParams_whenCallsAuthenticate_shouldReturnToken() throws Exception {
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";
        final var expectedToken = "authToken";

        when(createAuthenticateUseCase.execute(expectedEmail, expectedPassword))
                .thenReturn(Either.right(new AuthenticateTokenOutput(expectedToken)));

        final var input = new AuthenticateRequest(expectedEmail, expectedPassword);

        final var request = MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Matchers.equalTo(expectedToken)));
    }

    @Test
    public void givenAnInvalidEmail_whenCallsAuthenticate_shouldReturnNotFoundException() throws Exception {
        final var expectedEmail = "kaua@mail.com";
        final var expectedErrorMessage = "Account with ID kaua@mail.com was not found";

        when(createAuthenticateUseCase.execute(expectedEmail, "123456789"))
                .thenThrow(NotFoundException.with(Account.class, expectedEmail));

        final var input = new AuthenticateRequest(expectedEmail, "123456789");

        final var request = MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(input));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo(expectedErrorMessage)));
    }
}
