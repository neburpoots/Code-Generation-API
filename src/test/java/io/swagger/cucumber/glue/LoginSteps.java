package io.swagger.cucumber.glue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserLoginReturnDTO;
import io.swagger.model.user.UserPostDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class LoginSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    final String baseUrl = "http://localhost:";
    @LocalServerPort
    int serverPort;

    private UserLoginDTO loginUser;
    private ResponseEntity<String> response;
    private UserLoginReturnDTO actualUser;

    @Given("^the following login information$")
    public void givenTheFollowingInformation(final UserLoginDTO loginUser) {
        this.loginUser = loginUser;
    }

    @When("^the customer logs in")
    public void whenTheCustomerLogsIn() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(loginUser),
                httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/users/login",
                request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        actualUser = mapper.readValue(String.valueOf(jsonObject), new TypeReference<UserLoginReturnDTO>() {
        });
    }

    @Then("^their user details and a jwt token are returned")
    public void theUserDetailsAndJwtAreReturned() {
        validateOutput();
    }

    private void validateOutput() {
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertNotNull(actualUser.getUser_id());
        Assertions.assertEquals(loginUser.getEmail(), actualUser.getEmail());
        Assertions.assertNotNull(actualUser.getAccessToken());
        Assertions.assertNotNull(actualUser.getRefreshToken());
        Assertions.assertTrue(actualUser.getAccessToken().startsWith("ey"));
    }
}
