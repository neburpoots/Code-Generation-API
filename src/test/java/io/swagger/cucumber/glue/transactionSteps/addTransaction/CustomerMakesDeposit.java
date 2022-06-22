package io.swagger.cucumber.glue.transactionSteps.addTransaction;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.transactionSteps.TransactionBaseSteps;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.TransactionType;
import io.swagger.model.entity.User;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CustomerMakesDeposit extends TransactionBaseSteps {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    private TransactionPostDTO transaction;
    private ResponseEntity<TransactionGetDTO> response;
    private TransactionGetDTO createdTransaction;

    private Account accountBeforeDeposit;
    private Account accountAfterDeposit;

    @Given("the customer enters the following deposit information")
    public void theCustomerEntersTheFollowingDepositInformation(final TransactionPostDTO transaction) {
        this.transaction = transaction;
    }

    @When("the customer makes the transaction")
    public void theCustomerMakesTheTransaction() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");

        User user = userRepository.findByEmail("customer@student.inholland.nl");
        this.accountBeforeDeposit = this.accountService.retrieveAccount(transaction.getFromAccount());

        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

        HttpEntity<TransactionPostDTO> request = new HttpEntity<>(this.transaction, httpHeaders);

        response = restTemplate.postForEntity(baseUrl + serverPort + "/api/transactions",
                request, TransactionGetDTO.class);

        createdTransaction = response.getBody();

        this.accountAfterDeposit = this.accountService.retrieveAccount(transaction.getFromAccount());
    }

    @Then("a {int} created response with the created object is returned and deposit is added on balance")
    public void aCreatedResponseWithTheCreatedObjectIsReturnedAndDepositIsInDatabase(int statusCode) {
        this.validateOutput(statusCode);
    }

    private void validateOutput(int statusCode){
        Assertions.assertEquals(response.getStatusCodeValue(), statusCode);
        Assertions.assertEquals(TransactionType.deposit, createdTransaction.getType());
        Assertions.assertEquals(accountBeforeDeposit.getBalance().add(transaction.getAmount()), accountAfterDeposit.getBalance());
    }

}