package io.swagger.cucumber.glue.transactionSteps.addTransaction;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.model.entity.TransactionType;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeMakesTransaction extends TransactionBaseSteps {
    private TransactionPostDTO transaction;
    private ResponseEntity<TransactionGetDTO> response;
    private TransactionGetDTO createdTransaction;

    @Given("the employee enters the following transaction")
    public void theEmployeeEntersTheFollowingTransaction(final TransactionPostDTO transaction) {
        this.transaction = transaction;
    }
    @When("the employee creates the transaction")
    public void theEmployeeCreatesTheTransaction() throws JSONException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("tim@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                request, TransactionGetDTO.class);

        createdTransaction = response.getBody();
    }

    @Then("a {int} created response is returned together with the just created object")
    public void aCreatedResponseIsReturnedTogetherWithTheJustCreatedObject(int httpResponseCode) {
        this.validateOutput(httpResponseCode);
    }

    private void validateOutput(int httpResponseCode){
        Assertions.assertEquals(this.response.getStatusCodeValue(), httpResponseCode);
        Assertions.assertEquals(this.createdTransaction.getAmount(), transaction.getAmount());
        Assertions.assertEquals(this.createdTransaction.getFromAccount(), transaction.getFromAccount());
        Assertions.assertEquals(this.createdTransaction.getToAccount(), transaction.getToAccount());
        Assertions.assertEquals(TransactionType.regular_transaction, this.createdTransaction.getType());
    }
}