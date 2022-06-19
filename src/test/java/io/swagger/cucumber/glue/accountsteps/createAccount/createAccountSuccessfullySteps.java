package io.swagger.cucumber.glue.accountsteps.createAccount;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createAccountSuccessfullySteps  extends BaseAccountSteps {

    @Before("@createAccountSuccessfully")
    @Override
    public void setup() {
        super.setup();
    }

    private AccountPostDTO accountPostDTO;

    @Given("^the account post dto for the account that will be created with email (.*)$")
    public void givenTheAccountsForTheFilters(final String email, final List<Account> accounts)
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

    @When("^a employee posts a new account with the given data$")
    public void whenAEmployeeCreatesAnAccount()
    {
        System.out.println(accountPostDTO.getAccountType());
        System.out.println(accountPostDTO.getAbsoluteLimit());
        System.out.println(accountPostDTO.getUser_id());

        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", accountPostDTO.getAbsoluteLimit());
        map.put("account_type", accountPostDTO.getAccountType().toString());
        map.put("user_id", accountPostDTO.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, employeeHeaders);

        testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

    }

    @Then("^it is in the database$")
    public void thenItIsInTheDatabase() {
        actualAccounts.addAll(accountRepository.findAll());
        validateAccounts();
    }

    @And("^it has an id$")
    public void andItHasAnId() {
        Assertions.assertNotNull(actualAccounts.get(0).getAccount_id());
    }

}
