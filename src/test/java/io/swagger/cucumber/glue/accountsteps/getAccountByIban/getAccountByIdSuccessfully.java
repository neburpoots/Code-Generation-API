package io.swagger.cucumber.glue.accountsteps.getAccountByIban;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
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

public class getAccountByIdSuccessfully extends BaseAccountSteps {

    @Before("@getAccountByIdSuccessfully")
    @Override
    public void setup() {
        super.setup();
    }


    @Given("^the following account with the email (.*)$")
    public void givenTheFollowingAccounts(final String email, final List<Account> accounts)
    {
        Account expectedAccount = accounts.get(0);
        //Set the users for testing
        User testUser = userRepository.findByEmail(email);

        expectedAccount.setUser(testUser);

        //Save the expected user to the database and expected account
        expectedAccounts.add(accountRepository.save(expectedAccount));

    }

    @When("^the employee retrieves the account")
    public void whenTheUserRequestAllTheAccounts() throws IOException {

        HttpEntity request = new HttpEntity(employeeHeaders);

        ResponseEntity<Account> account =

                testRestTemplate.exchange("/api/accounts/" + expectedAccounts.get(0).getAccount_id(), HttpMethod.GET, request, new ParameterizedTypeReference<Account>() {
                });

        actualAccounts.add(account.getBody());
    }

    @Then("^the account is returned")
    public void thenAllTheAccountsAreReturned() {
        validateAccounts();
    }
}
