package io.swagger.cucumber.glue.accountsteps.createAccount;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createAccountForUserWithWrongAbsoluteLimitReturns400 extends BaseAccountSteps {

    private AccountPostDTO accountPostDTO;

    @Before("@createAccountForUserWithWrongAbsoluteLimitReturns400")
    @Override
    public void setup() {
        super.setup();
    }

    @Given("^the invalid account post dto for a new account and the email (.*)$")
    public void givenTheInvalidAccountPostDTOWithEmail(final String email, final List<Account> accounts)
    {
        //Get the customer for testing
        User testUser = userRepository.findByEmail(email);

        //Get all accounts for checking
        expectedAccounts.addAll(accountRepository.findAll());

        this.accountPostDTO = new AccountPostDTO()
                .user_id(testUser.getUser_id())
                .accountType(accounts.get(0).getAccountType())
                .absoluteLimit(accounts.get(0).getAbsoluteLimit());

    }

    @When("^a employee posts the invalid post data with the uuid$")
    public void whenAEmployeePostsANewAccountWithTheGivenDtoAndUUID() throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", accountPostDTO.getAbsoluteLimit());
        map.put("account_type", accountPostDTO.getAccountType().toString());
        map.put("user_id", accountPostDTO.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, employeeHeaders);


        ResponseEntity<String> error =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        System.out.println(error);
        //Converts String to object
        this.actualErrorMessage = objectMapper.readValue(error.getBody(), ErrorMessage.class);
    }

    @Then("the customer will receive a {int} bad request with the error message {string}")
    public void thenTheCustomerWillReceiveAWithTheErrorMessage(final Integer code, final String message) {
        this.expectedCode = code;
        this.expectedErrorMessage = message;

        validateErrorMessageWithoutStatusCodeParam();
    }

    @And("the account has not been added to the database")
    public void andTheAccountHasNotBeenAddedToTheDatabase() {

        this.actualAccounts.addAll(accountRepository.findAll());

        Assertions.assertEquals(actualAccounts.size(), expectedAccounts.size());
    }
}
