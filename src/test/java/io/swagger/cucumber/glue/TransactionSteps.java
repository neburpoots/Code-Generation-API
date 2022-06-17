package io.swagger.cucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.Transaction;
import io.swagger.repository.TransactionRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.TransactionService;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TransactionSteps {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction expectedTransaction;

    private List<Transaction> actualTransaction;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders headers;

    @Before
    public void setup()
    {
        expectedTransaction = new Transaction();
        actualTransaction = new ArrayList<>();
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "EMPLOYEE"));

        headers.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", roles, UUID.randomUUID()));
    }

    @Given("^the following transaction$")
    public void givenTheFollowingTransaction(final Transaction transaction)
    {
        this.expectedTransaction = transaction;
    }

    @When("^the employee requests all transactions")
    public void whenTheEmployeeCreatesTransaction() throws IOException {
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<List<Transaction>> transactions =

                testRestTemplate.exchange("/api/transactions/?page=0&pageSize=1", HttpMethod.GET, request, new ParameterizedTypeReference<List<Transaction>>() {
                });

        this.actualTransaction = transactions.getBody();
    }

    @Then("^transactions are returned")
    public void createdTransactionIsReturned() {
       validateTransactions(this.expectedTransaction, this.actualTransaction.get(0));
    }

    private void validateTransactions(final Transaction expected, final Transaction actual){
        Assertions.assertThat(expected.getFromAccount().equals(actual.getFromAccount()));
    }
}
