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
    public void givenAnEmptyStringsInParams_whenCallsNewAccount_thenInstantiateAccount() {
        final var expectedName = " ";
        final var expectedEmail = " ";
        final var expectedPassword = " ";

        final var expectedErrorMessageOne = "'name' should not be empty or null";
        final var expectedErrorMessageTwo = "'password' should not be empty or null";
        final var expectedErrorMessageThree = "'password' must contain 8 characters at least";
        final var expectedErrorMessageFour = "'email' should not be empty or null";

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);

        final var actualException = aAccount.validate();

        Assertions.assertEquals(expectedErrorMessageOne, actualException.get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.get(1).message());
        Assertions.assertEquals(expectedErrorMessageThree, actualException.get(2).message());
        Assertions.assertEquals(expectedErrorMessageFour, actualException.get(3).message());
    }
}
