package io.swagger.cucumber.glue.transactionSteps.getById;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.Objects;

public class CustomerGetTransactionByIdNoneMatching extends TransactionBaseSteps {
    @Autowired
    private UserRepository userRepository;

    private ResponseEntity<ErrorMessage> errorMessage;

    private String transactionId;

    @Given("the custome enters the following transaction id {string}")
    public void theCustomeEntersTheFollowingTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @When("the customer request the transaction endpoint with the given id")
    public void theCustomerRequestTheTransactionEndpointWithTheGivenId() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        User user = userRepository.findByEmail("customer@student.inholland.nl");

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

        HttpEntity<ErrorMessage> request = new HttpEntity<>(httpHeaders);

        errorMessage = restTemplate.exchange("/api/transactions/" + transactionId, HttpMethod.GET, request, new ParameterizedTypeReference<ErrorMessage>() {});
    }

    @Then("the customer will receive a error message and {int} response")
    public void theCustomerWillReceiveAErrorMessageAndResponse(int statusCode) {
        this.validateOutput(statusCode);
    }

    private void validateOutput(int statusCode){
        Assertions.assertEquals("No transaction found with matching ID. ", Objects.requireNonNull(errorMessage.getBody()).getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), errorMessage.getStatusCodeValue());
    }
}
