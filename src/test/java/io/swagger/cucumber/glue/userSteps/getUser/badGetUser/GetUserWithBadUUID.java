package io.swagger.cucumber.glue.userSteps.getUser.badGetUser;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.userSteps.getUser.GetUserBaseSteps;
import io.swagger.exception.ErrorMessage;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class GetUserWithBadUUID extends GetUserBaseSteps
{
    private String uuid;

    @Given("^A bad UUID of \"([^\"]*)\"$")
    public void givenAValidUUID(String uuid)
    {
        this.uuid = uuid;
    }

    @When("^A user is requested with the bad UUID")
    public void whenAUserIsRequested() throws Exception
    {
        System.out.println(uuid);
        response = restTemplate
                .exchange(baseUrl + serverPort + "/api/users/" + this.uuid,
                        HttpMethod.GET, new HttpEntity<>(getAuthorizedHeaders()),
                        String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>()
        {
        });
    }

    @Then("^A bad request error is returned with \"Invalid UUID string: 404' not found\" message")
    public void theErrorObjectIsReturned()
    {
        validateOutput();
    }

    private void validateOutput()
    {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Assertions.assertEquals("Invalid UUID string: " + uuid, output.getMessage());
    }
}
