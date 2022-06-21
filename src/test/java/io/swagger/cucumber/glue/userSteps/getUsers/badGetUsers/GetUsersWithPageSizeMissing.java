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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class GetUsersWithPageSizeMissing extends GetUsersBaseSteps
{
    @Given("^the pageNo of \"([^\"]*)\" and the pageSize missing for the getUsers endpoint$")
    public void givenAValidUUID(int pageNo)
    {
        this.pageNo = pageNo;
    }

    @When("^the user requests all the users with the given page info and the page size missing")
    public void whenAUserIsRequested() throws JSONException, IOException
    {
        errorResponse = restTemplate
                .exchange(baseUrl + serverPort + "/api/users?pageNo=" + pageNo,
                        HttpMethod.GET, new HttpEntity<>(getAuthorizedHeaders()),
                        new ParameterizedTypeReference<>()
                        {
                        });

        JSONObject jsonObject = new JSONObject(errorResponse.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>()
        {
        });
    }

    @Then("^a bad request error is returned with \"Required Integer parameter 'pageSize' is not present\" message")
    public void theErrorObjectIsReturned()
    {
        validateOutput();
    }

    private void validateOutput()
    {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCodeValue());
        Assertions.assertEquals("Required Integer parameter 'pageSize' is not present", output.getMessage());
    }
}
