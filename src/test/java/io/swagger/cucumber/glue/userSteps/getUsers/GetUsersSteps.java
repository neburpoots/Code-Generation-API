package io.swagger.cucumber.glue.userSteps.getUsers;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GetUsersSteps extends GetUsersBaseSteps
{
    @Given("^the pageNo of \"([^\"]*)\" and the pageSize of \"([^\"]*)\" for the getUsers endpoint$")
    public void givenAValidUUID(int pageNo, int pageSize)
    {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    @When("^the user requests all the users with the given page info")
    public void whenAUserIsRequested()
    {
        response = restTemplate
                .exchange(baseUrl + serverPort + "/api/users?pageNo=" + pageNo + "&pageSize=" + pageSize,
                        HttpMethod.GET, new HttpEntity<>(getAuthorizedHeaders()),
                        new ParameterizedTypeReference<>()
                        {
                        });

        actualUsers = Objects.requireNonNull(response.getBody()).getContent();
    }

    @Then("^a list of users is returned")
    public void theUserObjectPageIsReturned()
    {
        validateOutput();
    }

    private void validateOutput()
    {
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(pageSize, actualUsers.size());
        Assertions.assertTrue(Objects.requireNonNull(response.getBody()).isFirst());
        Assertions.assertEquals(pageNo, response.getBody().getPageable().getPageNumber());
    }
}
