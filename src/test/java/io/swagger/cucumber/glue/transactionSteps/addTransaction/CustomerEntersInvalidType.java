package io.swagger.cucumber.glue.transactionSteps.addTransaction;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.model.entity.TransactionType;
import io.swagger.model.entity.User;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Objects;

public class CustomerEntersInvalidType extends TransactionBaseSteps {
    @Autowired
    private UserRepository userRepository;

    private TransactionPostDTO transaction;
    private ResponseEntity<TransactionGetDTO> response;

    @Given("the customer enters the wrong number for transaction type")
    public void theCustomerEntersTheNumberForTransactionType(final TransactionPostDTO transaction) {
        this.transaction = transaction;
    }

    @When("the customer tries to create the transaction with the invalid type")
    public void theCustomerTriesToCreateTheTransactionWithTheInvalidType() {
        User customer = this.userRepository.findByEmail("customer@student.inholland.nl");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), customer.getUser_id()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                request, TransactionGetDTO.class);
    }

    @Then("a {int} bad request is returned telling the customer what was wrong")
    public void heABadRequestIsReturnedTellingTheCustomerWhatWasWrong(int statusCode) {
        this.validateOutput(statusCode);
    }

    public void validateOutput(int statusCode){
        Assertions.assertEquals(response.getStatusCodeValue(), statusCode);
        Assertions.assertNotEquals(TransactionType.regular_transaction, Objects.requireNonNull(response.getBody()).getType());
    }
}