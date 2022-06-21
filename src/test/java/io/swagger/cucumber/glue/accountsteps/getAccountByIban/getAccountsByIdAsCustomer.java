package io.swagger.cucumber.glue.accountsteps.getAccountByIban;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public class getAccountsByIdAsCustomer extends BaseAccountSteps {

    @Before("@getAccountsByIdAsCustomerReturns403")
    @Override
    public void setup() {
        super.setup();
    }


    @Given("^the following account to store in the database with user (.*)$")
    public void givenTheFollowingAccounts(final String email, final List<Account> accounts)
    {
        Account expectedAccount = accounts.get(0);
        //Set the users for testing
        User testUser = userRepository.findByEmail(email);

        expectedAccount.setUser(testUser);

        //Save the expected user to the database and expected account
        expectedAccounts.add(accountRepository.save(expectedAccount));

    }

    @When("^a customer tries to retrieve an account which is not his own")
    public void whenTheUserRequestAllTheAccounts() throws IOException {

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<String> error =
                testRestTemplate.exchange("/api/accounts/" + expectedAccounts.get(0).getAccount_id(), HttpMethod.GET, request, String.class);

        //Converts String to object
        this.actualErrorMessage = objectMapper.readValue(error.getBody(), ErrorMessage.class);
        System.out.println(expectedCode);
        System.out.println(actualErrorMessage.getStatusCode());
    }

    @Then("a {int} code should be returned and the following error message given {string}")
    public void then403IsReturnedWithAErrorMessage(final Integer code, final String message) {

        this.expectedCode = code;
        this.expectedErrorMessage = message;

        validateErrorMessageWithoutStatusCodeParam();
    }
}
