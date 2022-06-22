package io.swagger.cucumber.glue.transactionSteps.addTransaction;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.service.AccountService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.UUID;

public class CustomerGoesOverAbsoluteLimit extends TransactionBaseSteps {
    @Autowired
    private AccountService accountService;

    private TransactionPostDTO transaction;
    private ResponseEntity<String> response;
    private TransactionGetDTO createdTransaction;

    private ErrorMessage output;

    @Given("the employee wants to make the following transaction which goes under the absolute limit")
    public void theCustomerWantsToMakeTheFollowingTransactionWhichGoesUnderTheAbsoluteLimit(final TransactionPostDTO transaction) {
        this.transaction = transaction;
    }

    @When("the employee makes the transaction that will go under the limit")
    public void theCustomerMakesTheTransactionThatWillGoUnderTheLimit() throws JSONException {
            HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));

    HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        for(int i = 0; i <= 4; i++){
        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                request, String.class);
    }

        JSONObject object = new JSONObject(response.getBody());
        this.output = new Gson().fromJson(String.valueOf(object), ErrorMessage.class);
    }

    @Then("the employee will reaceive a {int} status and a message telling him his limit is reached")
    public void theCustomerWillReaceiveAStatusAndAMessageTellingHimHisLimitIsReached(int statusCode) {
        this.validateOutput(statusCode);
    }

    private void validateOutput(int statusCode){
        Assertions.assertEquals(response.getStatusCodeValue(), statusCode);
        Assertions.assertEquals("Account balance is insufficient to make this transaction. ", output.getMessage());
    }
}