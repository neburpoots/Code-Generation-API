package io.swagger.cucumber.glue.transactionSteps.getTransactions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.model.entity.Transaction;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TransactionsHaveAllAttributes extends TransactionBaseSteps {
    private ResponseEntity<RestPageImpl<Transaction>> response;
    private List<Transaction> actualTransactions;

    @Given("the customer wants to receive the transactions")
    public void theCustomerRetrievesTransactions() {
    }

    @When("the customer has received the transactions")
    public void theCustomerHasReceivedTheTransactions() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");
        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));

        response = restTemplate
                .exchange(baseUrl + serverPort + "/api/transactions",
                        HttpMethod.GET, new HttpEntity<>(httpHeaders),
                        new ParameterizedTypeReference<>() {
                        });

        this.actualTransactions = Objects.requireNonNull(response.getBody().getContent());
    }

    @Then("all transactions have an amount timestamp to and from account")
    public void allTransactionsHaveAnAmountTimestampToAndFromAccount() {
        this.validateOutput();
    }

    private void validateOutput(){
        for(Transaction transaction: actualTransactions){
            Assertions.assertTrue(transaction.getFromAccount() != null);
            Assertions.assertTrue(transaction.getToAccount() != null);
            Assertions.assertTrue(transaction.getAmount() != null);
            Assertions.assertTrue(transaction.getTimestamp() != null);
        }
    }
}
