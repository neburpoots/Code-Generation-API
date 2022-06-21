package io.swagger.cucumber.glue.userSteps.login.badLogin;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.userSteps.login.LoginBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.user.UserLoginDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class BadLoginPasswordSteps extends LoginBaseSteps
{
    @Given("^the following bad login password$")
    public void givenTheFollowingBadPassword(final UserLoginDTO loginUser)
    {
        this.loginUser = loginUser;
    }

    @When("^the customer logs in with the bad password")
    public void aCustomerLogsInWithABadPassword() throws Exception
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(loginUser),
                httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/users/login",
                request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>()
        {
        });
    }

    @Then("^an unauthorized error is returned")
    public void unauthorizedErrorIsReturned()
    {
        validateOutput();
    }

    private void validateOutput()
    {
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
        Assertions.assertEquals("Invalid login credentials", output.getMessage());
    }
}
