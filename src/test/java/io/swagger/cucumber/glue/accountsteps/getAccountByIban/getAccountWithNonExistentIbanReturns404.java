package io.swagger.cucumber.glue.accountsteps.getAccountByIban;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.Account;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class getAccountWithNonExistentIbanReturns404 extends BaseAccountSteps {


    @Before("@getAccountWithNonExistentIbanReturns404")
    @Override
    public void setup() {
        super.setup();
    }

    @When("^a employee retrieves the account with the id (.*)")
    public void whenTheUserRequestAllTheAccounts(final String iban) throws IOException {

        HttpEntity request = new HttpEntity(employeeHeaders);

        ResponseEntity<String> error =
                testRestTemplate.exchange("/api/accounts/" + iban, HttpMethod.GET, request, String.class);

        //Converts String to object
        this.actualErrorMessage = objectMapper.readValue(error.getBody(), ErrorMessage.class);
        System.out.println(expectedCode);
        System.out.println(actualErrorMessage.getStatusCode());
    }

    @Then("a {int} not found should be returned with the error message {string}")
    public void aShouldBeReturnedWithTheErrorMessage(final Integer code, final String message) {
        this.expectedCode = code;
        this.expectedErrorMessage = message;

        validateErrorMessageWithoutStatusCodeParam();
    }

}
