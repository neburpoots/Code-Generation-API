package io.swagger.cucumber.glue.transactionSteps.addTransaction;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class CustomerGoesOverDailyLimit extends TransactionBaseSteps {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    private TransactionPostDTO transaction;
    private ResponseEntity<String> response;
    private TransactionGetDTO createdTransaction;

    private ErrorMessage output;

    private int transactionRepitition;


    @Given("the customer executes the given transaction {int} times")
    public void theCustomerExecutesTheGivenTransactionTimes(int rep, final TransactionPostDTO transaction) {
        this.transactionRepitition = rep;
        this.transaction = transaction;
    }

    @When("the customer tries making the sixth transaction it will fail")
    public void theCustomerTriesMakingTheSixthTransactionItWillFail() throws JSONException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        User user = userRepository.findByEmail("customer@student.inholland.nl");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        for(int i = 0; i <= this.transactionRepitition; i++){
            response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                    request, String.class);
        }

        JSONObject object = new JSONObject(response.getBody());
        this.output = new Gson().fromJson(String.valueOf(object), ErrorMessage.class);
    }


    @Then("a {int} bad request is returned")
    public void aBadRequestIsReturned(int statusCode) {
        this.validateOutput(statusCode);
    }

    private void validateOutput(int statusCode){
        Assertions.assertEquals(this.output.getStatusCode(), statusCode);
        Assertions.assertEquals(this.output.getMessage(), "The daily limit has been reached, the transaction could not be performed. ");
    }
}
