package com.kaua.finances.domain.account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    public void givenAValidParams_whenCallsNewAccount_thenInstantiateAccount() {
        final var expectedName = "kaua";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "123";

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);

        Assertions.assertNotNull(aAccount);
        Assertions.assertNotNull(aAccount.getId());
        Assertions.assertEquals(expectedName, aAccount.getName());
        Assertions.assertEquals(expectedEmail, aAccount.getEmail());
        Assertions.assertEquals(expectedPassword, aAccount.getPassword());
        Assertions.assertNotNull(aAccount.getCreatedAt());
        Assertions.assertNotNull(aAccount.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallsNewAccount_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "123";
        final var expectedErrorMessage = "'name' should not be null";

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);

        final var actualException = aAccount.validate();

        Assertions.assertEquals(expectedErrorMessage, actualException.get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallsNewAccount_thenShouldReceiveError() {
        final var expectedName = " ";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "123";
        final var expectedErrorMessage = "'name' should not be empty";

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);

        final var actualException = aAccount.validate();

        Assertions.assertEquals(expectedErrorMessage, actualException.get(0).message());
    }
}
