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
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Objects;

public class CustomerGetByInvalidId extends TransactionBaseSteps {
    @Autowired
    private UserRepository userRepository;

    private ResponseEntity<ErrorMessage> errorMessage;

    private String transactionId;

    @Given("the customer uses the following string {string} as id")
    public void theCustomerUsesTheFollowingStringAsId(String transactionId) {
        this.transactionId = transactionId;
    }

    @When("the customer tries to request the transactio with the given id")
    public void theCustomerTriesToRequestTheTransactioWithTheGivenId() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        User user = userRepository.findByEmail("customer@student.inholland.nl");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

        HttpEntity<ErrorMessage> request = new HttpEntity<>(httpHeaders);

        errorMessage = restTemplate.exchange("/api/transactions/" + transactionId, HttpMethod.GET, request, new ParameterizedTypeReference<ErrorMessage>() {});
    }


    @Then("the customer will receive a error message and a {int}")
    public void theCustomerWillReceiveAErrorMessageAndA(int statusCode) {
        this.validateOutput(statusCode);

    }

    private void validateOutput(int statusCode){
        Assertions.assertEquals("Invalid UUID string: 64563453565436q534", Objects.requireNonNull(errorMessage.getBody()).getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorMessage.getStatusCodeValue());
    }
}
