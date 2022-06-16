package io.swagger.model.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountTest {

    @Test
    public void createNewAccountWithCorrectValuesAndCheckForNotNull() {
        Account account = new Account(new BigDecimal(500), new BigDecimal(-500), AccountType.PRIMARY, true);

        account.setUser(new User());
        account.setAccount_id("NLINHO00000001");

        assertNotNull(account.getBalance());
        assertNotNull(account.getAccountType());
        assertNotNull(account.getAbsoluteLimit());
        assertNotNull(account.getStatus());
        assertNotNull(account.getAccount_id());
        assertNotNull(account.getUser());

    }

}