package io.swagger.cucumber.glue.accountsteps.getAccounts;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class getAccountsNotLoggedInReturns403Steps extends BaseAccountSteps {

    @Before("@getAccountsAsCustomerReturns403")
    @Override
    public void setup() {
        super.setup();
    }

    private Integer statusCode;

    @When("a not logged in user retrieves the accounts and receives a {int} and a message containing {string}\"")
    public void aCustomerRetrievesTheAccountsAndReceivesErrorMessage(final Integer code, final String message) throws Throwable
    {
        this.expectedCode = code;

        this.expectedErrorMessage = message;

        ResponseEntity<String> error =
                testRestTemplate.exchange("/api/accounts", HttpMethod.GET, null, String.class);

        //Converts String to object
        this.actualErrorMessage = objectMapper.readValue(error.getBody(), ErrorMessage.class);
        this.statusCode = error.getStatusCodeValue();
    }

    @Then("^a not logged in user receives a 403 statuscode and receives a message")
    public void then403IsReturnedWithAErrorMessage() {
        validateErrorMessageWithStatusCode(statusCode);
    }
}
