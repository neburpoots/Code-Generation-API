package io.swagger.cucumber.glue.transactionSteps.getById;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class CustomerGetsStrangersTransactionById extends TransactionBaseSteps {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction actualTransaction;

    private TransactionPostDTO transaction;

    private ResponseEntity<ErrorMessage> errorMessage;

    @Given("the customer enters the matching id of a strangers transaction id")
    public void theCustomerEntersTheMatchingIdOfAStrangersTransactionId() {
        actualTransaction = transactionRepository.findAll().get(0);
    }

    @When("the customer request the endpoint")
    public void theCustomerRequestTheEndpoint() {
        String id = actualTransaction.getTransaction_id().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        User user = userRepository.findByEmail("customer@student.inholland.nl");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

        HttpEntity<ErrorMessage> request = new HttpEntity<>(httpHeaders);

        errorMessage = restTemplate.exchange("/api/transactions/" + id, HttpMethod.GET, request, new ParameterizedTypeReference<ErrorMessage>() {});
    }

    @Then("the customer will receive an error message and a {int} not found error")
    public void theCustomerWillReceiveAnErrorMessageAndANotFoundError(int statusCode) {
        this.validateOutput(statusCode);
    }

    private void validateOutput(int statusCode){
        Assertions.assertEquals(statusCode, this.errorMessage.getStatusCodeValue());
        Assertions.assertEquals("No Transaction found.", errorMessage.getBody().getMessage());
    }
}