package io.swagger.cucumber.glue.transactionSteps.getTransactions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.transaction.TransactionPostDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.*;

import java.util.*;
public class BadTransactionToStrangersSavingsAccount extends TransactionBaseSteps {
    private TransactionPostDTO transaction;
    private ResponseEntity<String> response;
    private ErrorMessage output;

    @Given("the customer enters following transaction information")
    public void theCustomerEntersFollowingTransactionInformation(final TransactionPostDTO transaction) {
        this.transaction = transaction;
    }

    @When("the employee makes the transaction")
    public void theEmployeeMakesTheTransaction() throws JSONException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");
        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                request, String.class);

        JSONObject object = new JSONObject(response.getBody());
        this.output = new Gson().fromJson(String.valueOf(object), ErrorMessage.class);
    }

    @Then("a {int} response is returned together with an error message.")
    public void aResponseIsReturnedTogetherWithAnErrorMessage(int statusCode) {
        Assertions.assertEquals(this.response.getStatusCodeValue(), statusCode);
        Assertions.assertEquals("You can't transfer money to this account. ", this.output.getMessage());
    }
}
