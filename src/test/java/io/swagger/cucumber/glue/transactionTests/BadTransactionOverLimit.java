package io.swagger.cucumber.glue.transactionTests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.User;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class BadTransactionOverLimit {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private TransactionPostDTO transaction;

    final String baseUrl = "http://localhost:";

    @LocalServerPort
    int serverPort;

    private ResponseEntity<String> response;

    private ErrorMessage output;

    @Given("the following transaction information")
    public void theFollowingTransactionInformation(final TransactionPostDTO transaction) {
        this.transaction = transaction;
    }

    @When("the customer tries to make this transaction")
    public void theCustomerTriesToMakeThisTransaction() throws JSONException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        User user = userRepository.findByEmail("customer@student.inholland.nl");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                request, String.class);

        JSONObject object = new JSONObject(response.getBody());
        this.output = new Gson().fromJson(String.valueOf(object), ErrorMessage.class);
    }

    @Then("he will receive a {int} bad request exception with an error message")
    public void heWillReceiveABadRequestExceptionWithAnErrorMessage(int httpResponseCode) {
        this.validateOutput(httpResponseCode);
    }

    private void validateOutput(int httpResponseCode){
        Assertions.assertEquals(this.response.getStatusCodeValue(), httpResponseCode);
        Assertions.assertEquals(this.output.getMessage(), "Amount was higher than transaction limit of (500.00).");
    }
}