package io.swagger.cucumber.glue.accountsteps.getAccounts;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getAccountsSuccessfullySteps extends BaseAccountSteps {

    @Before("@getAccountsSuccessfully")
    @Override
    public void setup() {
        super.setup();
    }


    @Given("^the following accounts$")
    public void givenTheFollowingAccounts(final List<Account> accounts)
    {
        expectedAccounts.addAll(accounts);

        //Set the users for testing
        List<User> testUser = userRepository.findAll();

        expectedAccounts.forEach(c -> c.setUser(testUser.get(0)));

        //Might need users
        accountRepository.saveAll(accounts);
    }

    @When("^the user requests all the accounts")
    public void whenTheUserRequestAllTheAccounts() throws IOException {

        HttpEntity request = new HttpEntity(employeeHeaders);

        ResponseEntity<RestPageImpl<Account>> accounts =

                testRestTemplate.exchange("/api/accounts", HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        actualAccounts = accounts.getBody().getContent();

    }

    @Then("^all the accounts are returned")
    public void thenAllTheAccountsAreReturned() {
        validateAccounts();
    }
}
