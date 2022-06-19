package io.swagger.cucumber.glue.accountsteps.getAccounts;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class getAccountsAsEmployeeWithNonExistentUserIdSteps extends BaseAccountSteps {

    @Before("@getAccountsAsEmployeeWithNonExistentUserIdSteps")
    @Override
    public void setup() {
        super.setup();
    }

    @When("a employee retrieves the accounts with the id (.*)$")
    public void whenAEmployeeRetrievesAccountsForNonExistentUser(final String userId) throws Throwable
    {

        HttpEntity request = new HttpEntity(employeeHeaders);

        ResponseEntity<String> error =
                testRestTemplate.exchange("/api/accounts?user_id=" + userId, HttpMethod.GET, request, String.class);

        //Converts String to object
        this.actualErrorMessage = objectMapper.readValue(error.getBody(), ErrorMessage.class);
        System.out.println(expectedCode);
        System.out.println(actualErrorMessage.getStatusCode());
    }

    @Then("a {int} should be returned with the error message {string}")
    public void aShouldBeReturnedWithTheErrorMessage(final Integer code, final String message) {
        this.expectedCode = code;
        this.expectedErrorMessage = message;

        validateErrorMessageWithoutStatusCodeParam();
    }
}
