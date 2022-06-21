package io.swagger.cucumber.glue.userSteps.getUsers.badGetUsers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.userSteps.getUsers.GetUsersBaseSteps;
import io.swagger.exception.ErrorMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class GetUsersNotLoggedIn extends GetUsersBaseSteps
{
    @Given("^the pageNo of \"([^\"]*)\" and the pageSize of \"([^\"]*)\" for the getUsers endpoint while not logged in$")
    public void givenAValidUUID(int pageNo, int pageSize)
    {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    @When("^the user requests all the users with the given page info while not being logged in")
    public void whenAUserIsRequested() throws JSONException, IOException
    {
        errorResponse = restTemplate
                .exchange(baseUrl + serverPort + "/api/users?pageNo=" + pageNo + "&pageSize=" + pageSize,
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<>()
                        {
                        });

        JSONObject jsonObject = new JSONObject(errorResponse.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>()
        {
        });
    }

    @Then("a forbidden error is returned with {string} message")
    public void theUserObjectIsReturned(String message)
    {
        validateOutput(message);
    }

    private void validateOutput(String message)
    {
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(), errorResponse.getStatusCodeValue());
        Assertions.assertEquals(message, output.getMessage());
    }
}
