package io.swagger.cucumber.glue.badLogin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.user.UserLoginDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MissingLoginEmailSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    final String baseUrl = "http://localhost:";
    @LocalServerPort
    int serverPort;

    private UserLoginDTO loginUser;
    private ResponseEntity<String> response;
    private ErrorMessage output;

    @Given("^the following bad login information with missing email$")
    public void givenTheFollowingInfoWithMissingEmail(final UserLoginDTO loginUser) {
        this.loginUser = loginUser;
    }

    @When("^the customer logs in with the email missing")
    public void aCustomerLogsInWithMissingEmail() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(loginUser),
                httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/users/login",
                request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>() {
        });
    }

    @Then("^a bad request error is returned with \"email missing\" message")
    public void badRequestErrorForEmailIsReturned() {
        validateOutput();
    }

    private void validateOutput() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Assertions.assertEquals("Email missing", output.getMessage());
    }
}
