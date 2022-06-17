package io.swagger.cucumber.glue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.user.UserGetDTO;
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

public class RegistrationSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    final String baseUrl = "http://localhost:";
    @LocalServerPort
    int serverPort;

    private UserPostDTO registrationUser;
    private ResponseEntity<String> response;
    private UserGetDTO actualUser;

    @Given("^the following information$")
    public void givenTheFollowingInformation(final UserPostDTO registrationUser) {
        this.registrationUser = registrationUser;
    }

    @When("^the customer registers with the given information")
    public void whenTheCustomerRegisters() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(registrationUser),
                httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/users/register",
                request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        actualUser = mapper.readValue(String.valueOf(jsonObject), new TypeReference<UserGetDTO>() {
        });
    }

    @Then("^their user details are returned")
    public void theUserDetailsAreReturned() {
        validateOutput();
    }

    private void validateOutput() {
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertNotNull(actualUser.getUser_id());
        Assertions.assertEquals(registrationUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(registrationUser.getFirstname(), actualUser.getFirstname());
        Assertions.assertEquals(registrationUser.getLastname(), actualUser.getLastname());
        Assertions.assertEquals(new BigDecimal(2500), actualUser.getDailyLimit());
        Assertions.assertEquals(new BigDecimal(50), actualUser.getTransactionLimit());
        Assertions.assertEquals(1, actualUser.getRoles().size());
        Assertions.assertEquals(1, actualUser.getRoles().get(0).getRole_id().intValue());
        Assertions.assertEquals("ROLE_CUSTOMER", actualUser.getRoles().get(0).getName());
    }
}
