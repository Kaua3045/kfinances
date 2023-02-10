package com.kaua.finances.infrastructure.account;

import com.kaua.finances.domain.account.Account;
import com.kaua.finances.infrastructure.MySQLGatewayTest;
import com.kaua.finances.infrastructure.account.persistence.AccountJpaFactory;
import com.kaua.finances.infrastructure.account.persistence.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class AccountMySQLGatewayTest {

    @Autowired
    private AccountMySQLGateway accountGateway;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void givenAValidParams_whenCallsCreate_shouldReturnANewAccount() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "12345678";

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);

        Assertions.assertEquals(0, accountRepository.count());

        final var actualAccount = accountGateway.create(aAccount);

        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(expectedName, actualAccount.getName());
        Assertions.assertEquals(expectedEmail, actualAccount.getEmail());
        Assertions.assertEquals(expectedPassword, actualAccount.getPassword());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());

        final var actualEntity = accountRepository.findById(aAccount.getId()).get();

        Assertions.assertEquals(aAccount.getId(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedEmail, actualEntity.getEmail());
        Assertions.assertEquals(expectedPassword, actualEntity.getPassword());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    public void givenAValidParams_whenCallsUpdate_shouldReturnAccountUpdated() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "123456789";

        final var aAccount = Account.newAccount("kau", expectedEmail, "12345678");

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.saveAndFlush(AccountJpaFactory.from(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        final var actualAccountEntity = accountRepository.findById(aAccount.getId()).get();

        Assertions.assertEquals("kau", actualAccountEntity.getName());
        Assertions.assertEquals(expectedEmail, actualAccountEntity.getEmail());
        Assertions.assertEquals("12345678", actualAccountEntity.getPassword());

        final var aUpdatedAccount = Account.with(aAccount).update(
                expectedName,
                expectedPassword
        );

        final var actualAccount = accountGateway.update(aUpdatedAccount);

        Assertions.assertEquals(1, accountRepository.count());

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(expectedName, actualAccount.getName());
        Assertions.assertEquals(expectedEmail, actualAccount.getEmail());
        Assertions.assertEquals(expectedPassword, actualAccount.getPassword());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertTrue(actualAccount.getUpdatedAt().isAfter(aAccount.getUpdatedAt()));

        final var actualEntity = accountRepository.findById(aAccount.getId()).get();

        Assertions.assertEquals(aAccount.getId(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedEmail, actualEntity.getEmail());
        Assertions.assertEquals(expectedPassword, actualEntity.getPassword());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(actualEntity.getUpdatedAt().isAfter(aAccount.getUpdatedAt()));
    }

    @Test
    public void givenAValidId_whenCallsGetById_shouldReturnAccount() {
        final var expectedName = "kauã";
        final var expectedEmail = "kaua@mail.com";
        final var expectedPassword = "123456789";

        final var aAccount = Account.newAccount(expectedName, expectedEmail, expectedPassword);

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.saveAndFlush(AccountJpaFactory.from(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        final var actualAccount = accountGateway.findById(aAccount.getId()).get();

        Assertions.assertEquals(aAccount.getId(), actualAccount.getId());
        Assertions.assertEquals(expectedName, actualAccount.getName());
        Assertions.assertEquals(expectedEmail, actualAccount.getEmail());
        Assertions.assertEquals(expectedPassword, actualAccount.getPassword());
        Assertions.assertEquals(aAccount.getCreatedAt(), actualAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), actualAccount.getUpdatedAt());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldBeOk() {
        final var aAccount = Account.newAccount("kauã", "kaua@mail.com", "12345678");

        Assertions.assertEquals(0, accountRepository.count());

        accountRepository.saveAndFlush(AccountJpaFactory.from(aAccount));

        Assertions.assertEquals(1, accountRepository.count());

        accountGateway.deleteById(aAccount.getId());

        Assertions.assertEquals(0, accountRepository.count());
    }
}
