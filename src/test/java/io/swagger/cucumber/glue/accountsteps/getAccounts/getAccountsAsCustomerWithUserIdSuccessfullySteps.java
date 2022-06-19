package io.swagger.cucumber.glue.accountsteps.getAccounts;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.utils.RestPageImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class getAccountsAsCustomerWithUserIdSuccessfullySteps extends BaseAccountSteps {

    private UUID userId;

    @Before("@getAccountsSuccessfullyForCustomerWithUserId")
    @Override
    public void setup() {
        super.setup();
    }


    @Given("^the customers own accounts$")
    public void givenTheCustomersOwnAccounts(final List<Account> accounts)
    {
        expectedAccounts.addAll(accounts);

        //Get the customer for testing
        User testUser = userRepository.findByEmail("customer@student.inholland.nl");

        expectedAccounts.forEach(c -> c.setUser(testUser));

        this.userId = testUser.getUser_id();

        //Save the accounts to the repository for checking
        accountRepository.saveAll(accounts);
    }

    @When("^the customer requests all the accounts with his user id as url parameter")
    public void whenTheCustomerRequestsAllTheAccountsWithUserId() throws IOException {

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<RestPageImpl<Account>> accounts =

                testRestTemplate.exchange("/api/accounts?user_id=" + userId, HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        actualAccounts = accounts.getBody().getContent();

    }

    @Then("^all the accounts are returned for the specific customer")
    public void thenAllTheAccountsAreReturnedForCustomer() {
        validateAccounts();
    }
}
