package io.swagger.cucumber.glue.badRegistrationSteps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserLoginDTO;
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

public class BadRegistrationEmailSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    final String baseUrl = "http://localhost:";
    @LocalServerPort
    int serverPort;

    private UserPostDTO registrationUser;
    private ResponseEntity<String> response;
    private ErrorMessage output;

    @Given("^the following register information with bad email$")
    public void givenTheFollowingBadEmailForRegistration(final UserPostDTO registrationUser) {
        this.registrationUser = registrationUser;
    }

    @When("^the customer registers with the given information with bad email")
    public void aCustomerRegistersWithABadEmail() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(registrationUser),
                httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/users/register",
                request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        output = mapper.readValue(String.valueOf(jsonObject), new TypeReference<ErrorMessage>() {
        });
    }

    @Then("^a bad request error is returned with \"Email address is invalid\" message")
    public void badRequestErrorForRegisterEmailIsReturned() {
        validateOutput();
    }

    private void validateOutput() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        Assertions.assertEquals("Email address is invalid", output.getMessage());
    }
}
