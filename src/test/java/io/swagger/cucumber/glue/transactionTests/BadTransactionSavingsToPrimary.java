package io.swagger.cucumber.glue.transactionTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.Transaction;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.security.JwtTokenProvider;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BadTransactionSavingsToPrimary {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private TransactionPostDTO transaction;

    final String baseUrl = "http://localhost:";

    @LocalServerPort
    int serverPort;

    private ResponseEntity<String> response;

    private ErrorMessage output;

    @Given("the following create transaction")
    public void the_following_create_transaction(final TransactionPostDTO transaction) {
        this.transaction = transaction;
    }

    @When("^a customer tries to make the transaction")
    public void aCustomerTriesToMakeTheTransaction() throws IOException, JSONException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");
        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                request, String.class);


        JSONObject object = new JSONObject(response.getBody());
        this.output = new Gson().fromJson(String.valueOf(object), ErrorMessage.class);
    }

    @Then("a {int} unauthorized exception is thrown")
    public void aUnauthorizedExceptionIsThrown(int httpResponseCode) {
        this.validateOutput(httpResponseCode);
    }
    //Validates return response was 401 unauthorized && checks if the returned error message is supplied.
    private void validateOutput(int httpResponseCode){
        Assertions.assertEquals(this.output.getStatusCode(), httpResponseCode);
        Assertions.assertEquals("No access, you can only make/view transactions from your own account(s).", this.output.getMessage());
    }


}
