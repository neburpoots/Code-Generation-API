package io.swagger.cucumber.glue.userSteps.getUser;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.user.UserGetDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GetUserSteps extends GetUserBaseSteps
{
    @Before("@getUser")
    public void setup()
    {
        makeExampleUser();
    }

    @Given("^A valid UUID$")
    public void givenAValidUUID()
    {
        this.uuid = exampleUser.getUser_id();
    }

    @When("^A user is requested")
    public void whenAUserIsRequested() throws Exception
    {
        response = restTemplate
                .exchange(baseUrl + serverPort + "/api/users/" + uuid,
                        HttpMethod.GET, new HttpEntity<>(getAuthorizedHeaders()),
                        String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        actualUser = mapper.readValue(String.valueOf(jsonObject), new TypeReference<UserGetDTO>()
        {
        });
    }

    @Then("^The user object is returned")
    public void theUserObjectIsReturned()
    {
        validateOutput();
    }

    private void validateOutput()
    {
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(uuid, actualUser.getUser_id());
        Assertions.assertEquals(exampleUser.getEmail(), actualUser.getEmail());
    }
}
