package io.swagger.cucumber.glue.transactionSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerGetsTransactions extends TransactionBaseSteps {
   @Autowired
   private UserRepository userRepository;
   private String amountEquals;
   private ResponseEntity<RestPageImpl<Transaction>> response;
   private List<Transaction> actualTransactions;

   @Given("the customer enters the following filter parameter as amountEqual {string}")
   public void theCustomerEntersTheFollowingFilterParameterAsAmountEqual(String amountEqual) {
      this.amountEquals = amountEqual;
   }

   @When("the customer request the transaction")
   public void theCustomerRequestTheTransaction() {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("Content-Type",
              "application/json");
      User user = this.userRepository.findByEmail("customer@student.inholland.nl");
      httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", new ArrayList<>(), user.getUser_id()));

      response = restTemplate
              .exchange(baseUrl + serverPort + "/api/transactions?amountEqual=" + amountEquals,
                      HttpMethod.GET, new HttpEntity<>(httpHeaders),
                      new ParameterizedTypeReference<>() {
                      });

      this.actualTransactions = Objects.requireNonNull(response.getBody()).getContent();
   }

   @Then("transactions are returned, status code {int} and transactions amount is {int}")
   public void transactionsAreReturnedStatusCodeAndTransactionsAmountIs(int statusCode, int amount) {
      this.validateOutput(statusCode, amount);
   }

   private void validateOutput(int statusCode, int amount){
      Assertions.assertEquals(response.getStatusCodeValue(), statusCode);

      for(Transaction transaction : actualTransactions){
         Assertions.assertEquals(0, transaction.getAmount().compareTo(new BigDecimal(amount)));
      }
   }
}
