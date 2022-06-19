package io.swagger.cucumber.glue.accountsteps.createAccount;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createAccountForUserThatHas2AccountsReturns409 extends BaseAccountSteps {

    private AccountPostDTO accountPostDTO;

    @Before("@createAccountForUserThatHas2AccountsReturns409")
    @Override
    public void setup() {
        super.setup();

        //Sets up accounts to repo for testing conflict
        User testUser = userRepository.findByEmail("ruben@student.inholland.nl");

        //Mock accounts to create a conflict error
        Account newAccount1 = new Account(new BigDecimal(0), new BigDecimal(0), AccountType.PRIMARY, true);
        Account newAccount2 = new Account(new BigDecimal(0), new BigDecimal(0), AccountType.SAVINGS, true);

        newAccount1.setUser(testUser);
        newAccount2.setUser(testUser);

        accountRepository.save(newAccount1);
        accountRepository.save(newAccount2);

    }

    @Given("^the valid account post dto for a new account and the email (.*)$")
    public void givenTheValidAccountPostDTOWithEmail(final String email, final List<Account> accounts)
    {
        //Get the customer for testing
        User testUser = userRepository.findByEmail(email);

        //Create a full account to check
        Account newAccount = new Account(new BigDecimal(0), accounts.get(0).getAbsoluteLimit(), accounts.get(0).getAccountType(), true);

        expectedAccounts.add(newAccount);

        expectedAccounts.forEach(c -> c.setUser(testUser));

        this.accountPostDTO = new AccountPostDTO()
                .user_id(testUser.getUser_id())
                .accountType(newAccount.getAccountType())
                .absoluteLimit(newAccount.getAbsoluteLimit());

    }

    @When("^a employee posts a new accounts with the given dto and uuid$")
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

    @Then("the customer will receive a {int} conflict with the error message {string}")
    public void theCustomerWillReceiveAWithTheErrorMessage(final Integer code, final String message) {
        this.expectedCode = code;
        this.expectedErrorMessage = message;

        validateErrorMessageWithoutStatusCodeParam();
    }
}
