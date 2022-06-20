package io.swagger.cucumber.glue.transactionSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.entity.Transaction;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeGetsTransactions extends TransactionBaseSteps {
    private String fromIban;
    private ResponseEntity<RestPageImpl<Transaction>> response;
    private List<Transaction> actualTransactions;

    @Given("the fromIban of {string} filter parameter is applied")
    public void theFromIbanOfFilterParameterIsApplied(String fromIban) {
        this.fromIban = fromIban;
    }

    @When("^a employee requests the transaction endpoint")
    public void whenAUserIsRequested() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");
        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));

        response = restTemplate
                .exchange(baseUrl + serverPort + "/api/transactions?fromIban=" + this.fromIban,
                        HttpMethod.GET, new HttpEntity<>(httpHeaders),
                        new ParameterizedTypeReference<>() {
                        });

       this.actualTransactions = Objects.requireNonNull(response.getBody().getContent());
    }
    @Then("^a list of transactions is returned")
    public void aListOfTransactionsIsReturned() {
        validateOutput();
    }
    private void validateOutput() {
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        for(Transaction transaction : this.actualTransactions){
            Assertions.assertEquals(transaction.getFromAccount(), this.fromIban);
        }
    }
}