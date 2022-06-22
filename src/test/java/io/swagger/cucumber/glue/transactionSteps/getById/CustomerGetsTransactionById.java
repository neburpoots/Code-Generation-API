package io.swagger.cucumber.glue.transactionSteps.getById;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;

public class CustomerGetsTransactionById extends TransactionBaseSteps {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction actualTransaction;

    private TransactionPostDTO transaction;

    private ResponseEntity<TransactionGetDTO> requestedTransaction;

    @Given("the customer enters a matching id")
    public void theCustomerEntersAMatchingId() {
        actualTransaction = transactionRepository.findAll().get(2);
    }

    @When("the customer request the getTransactionById endpoint")
    public void theCustomerRequestTheGetTransactionByIdEndpoint() {
        String id = actualTransaction.getTransaction_id().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        User user = userRepository.findByEmail("customer@student.inholland.nl");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        requestedTransaction = restTemplate.exchange("/api/transactions/" + id, HttpMethod.GET, request, new ParameterizedTypeReference<TransactionGetDTO>() {});
    }


    @Then("the customer will receive the transaction object and {int} status")
    public void theCustomerWillReceiveTheTransactionObjectAndStatus(int statusCode) {
        this.validateOutput(statusCode);
    }

    public void validateOutput(int statusCode){
        Assertions.assertEquals(this.actualTransaction.getFromAccount(), this.requestedTransaction.getBody().getFromAccount());
        Assertions.assertEquals(this.actualTransaction.getToAccount(), this.requestedTransaction.getBody().getToAccount());
        Assertions.assertEquals(this.actualTransaction.getAmount(), this.requestedTransaction.getBody().getAmount());
        Assertions.assertEquals(this.actualTransaction.getTransactionType(), this.requestedTransaction.getBody().getType());
        Assertions.assertEquals(statusCode, this.requestedTransaction.getStatusCodeValue());
    }
}
