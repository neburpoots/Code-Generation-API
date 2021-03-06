package io.swagger.cucumber.glue.userSteps.registration.badRegistrationSteps;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.userSteps.registration.RegistrationBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.user.UserPostDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class BadRegistrationEmailSteps extends RegistrationBaseSteps
{
    @Given("^the following register information with bad email$")
    public void givenTheFollowingBadEmailForRegistration(final UserPostDTO registrationUser)
    {
        this.registrationUser = registrationUser;
    }

    @When("^the customer registers with the given information with bad email")
    public void aCustomerRegistersWithABadEmail() throws Exception
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(registrationUser),
                httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/users/register",
                request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>()
        {
        });
    }

    @Then("^a bad request error is returned with \"Email address is invalid\" message")
    public void badRequestErrorForRegisterEmailIsReturned()
    {
        validateOutput();
    }

    private void validateOutput()
    {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Assertions.assertEquals("Email address is invalid", output.getMessage());
    }
}
