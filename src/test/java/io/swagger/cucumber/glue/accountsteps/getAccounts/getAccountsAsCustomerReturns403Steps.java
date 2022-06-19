package io.swagger.cucumber.glue.accountsteps.getAccounts;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Date;


public class getAccountsAsCustomerReturns403Steps extends BaseAccountSteps {

    @Before("@getAccountsAsCustomerReturns403")
    @Override
    public void setup() {
        super.setup();
    }

    @When("a customer retrieves the accounts and receives a {int} and a message containing {string}\"")
    public void aCustomerRetrievesTheAccountsAndReceivesErrorMessage(final Integer code, final String message) throws Throwable
    {

        this.expectedCode = code;
        this.expectedErrorMessage = message;

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<String> error =
                testRestTemplate.exchange("/api/accounts", HttpMethod.GET, request, String.class);

        //Converts String to object
        this.actualErrorMessage = objectMapper.readValue(error.getBody(), ErrorMessage.class);
        System.out.println(expectedCode);
        System.out.println(actualErrorMessage.getStatusCode());
    }

    @Then("^a customer receives a 403 statuscode and receives a message$")
    public void then403IsReturnedWithAErrorMessage() {
        validateErrorMessageWithoutStatusCodeParam();
    }
}
