package io.swagger.cucumber.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.user.UserGetDTO;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GetUsersSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    final String baseUrl = "http://localhost:";
    @LocalServerPort
    int serverPort;

    private ResponseEntity<RestPageImpl<UserGetDTO>> response;
    private List<UserGetDTO> actualUsers;

    private int pageNo;
    private int pageSize;

    @Given("^the pageNo of \"([^\"]*)\" and the pageSize of \"([^\"]*)\" for the getUsers endpoint$")
    public void givenAValidUUID(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    @When("^the user requests all the users with the given page info")
    public void whenAUserIsRequested() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");
        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));

        response = restTemplate
                .exchange(baseUrl + serverPort + "/api/users?pageNo=" + pageNo + "&pageSize=" + pageSize,
                        HttpMethod.GET, new HttpEntity<>(httpHeaders),
                        new ParameterizedTypeReference<>() {
                        });

        actualUsers = Objects.requireNonNull(response.getBody()).getContent();
    }

    @Then("^a list of users is returned")
    public void theUserObjectIsReturned() {
        validateOutput();
    }

    private void validateOutput() {
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(pageSize, actualUsers.size());
        Assertions.assertTrue(Objects.requireNonNull(response.getBody()).isFirst());
        Assertions.assertEquals(pageNo, response.getBody().getPageable().getPageNumber());
    }
}
