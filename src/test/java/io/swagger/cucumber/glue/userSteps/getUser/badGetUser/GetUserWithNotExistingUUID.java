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

import java.util.UUID;

public class GetUserWithNotExistingUUID extends GetUserBaseSteps
{
    @Given("^A not existing UUID of \"([^\"]*)\"$")
    public void givenAValidUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    @When("^A user is requested with the not existing UUID")
    public void whenAUserIsRequested() throws Exception
    {
        System.out.println(uuid);
        response = restTemplate
                .exchange(baseUrl + serverPort + "/api/users/" + uuid,
                        HttpMethod.GET, new HttpEntity<>(getAuthorizedHeaders()),
                        String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>()
        {
        });
    }

    @Then("^A resource not found error is returned with \"User with id: '123e4567-e89b-12d3-a456-426614174000' not found\" message")
    public void theErrorObjectIsReturned()
    {
        validateOutput();
    }

    private void validateOutput()
    {
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        Assertions.assertEquals("User with id: '" + uuid + "' not found", output.getMessage());
    }
}
