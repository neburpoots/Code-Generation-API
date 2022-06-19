package io.swagger.cucumber.glue.accountsteps.getAccounts;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.utils.RestPageImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class getAccountsWithAllFiltersSuccessfullySteps extends BaseAccountSteps {

    private UUID userId;

    @Before("@getAccountsSuccessfullyWithAllFilters")
    @Override
    public void setup() {
        super.setup();
    }


    @Given("^the account that the employee will retrieve with the filters$")
    public void givenTheAccountsForTheFilters(final List<Account> accounts)
    {
        expectedAccounts.addAll(accounts);

        //Get the customer for testing
        User testUser = userRepository.findByEmail("customer@student.inholland.nl");

        expectedAccounts.forEach(c -> c.setUser(testUser));

        this.userId = testUser.getUser_id();

        //Save the accounts to the repository for checking
        accountRepository.saveAll(accounts);
    }

    @When("^the employee requests the accounts for a user with accountType (.*) and pagination$")
    public void whenTheEmployeeRequestsAllTheAccountsWithTheFilters(final String accountType) throws IOException {

        HttpEntity request = new HttpEntity(employeeHeaders);

        ResponseEntity<RestPageImpl<Account>> accounts =

                testRestTemplate.exchange("/api/accounts?user_id=" + userId + "&type=" + accountType + "&page=0&size=1", HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        actualAccounts = accounts.getBody().getContent();

    }

    @Then("^the accounts that matches all the filters is returned")
    public void thenAllTheAccountsAreReturnedForCustomer() {
        validateAccounts();
    }
}
