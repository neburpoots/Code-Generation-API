package io.swagger.cucumber.glue.accountsteps.editAccount;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.model.account.AccountPatchDTO;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class editAccountSuccessfully extends BaseAccountSteps {

    private AccountPatchDTO accountPatchDTO;

    @Before("@editAccountSuccessfully")
    @Override
    public void setup() {
        super.setup();

        //Sets up account for editing
        User testUser = userRepository.findByEmail("ruben@student.inholland.nl");

        //Mock account to edit
        Account newAccount1 = new Account(new BigDecimal(0), new BigDecimal(0), AccountType.PRIMARY, true);

        newAccount1.setUser(testUser);

        //Adds the new account to the expected accounts
        expectedAccounts.add(accountRepository.save(newAccount1));
    }


    @Given("^the valid account patch dto for the primary account of ruben@student.inholland.nl")
    public void givenTheValidAccountPatchDto(final List<Account> accounts)
    {
        //Sets up the dto for the request with data from the scenario
        this.accountPatchDTO = new AccountPatchDTO()
                .status(accounts.get(0).getStatus())
                .absoluteLimit(accounts.get(0).getAbsoluteLimit());
    }

    @When("^a employee patches the valid patch dto for an account with the iban$")
    public void whenAEmployeePatchesAndAccountWithDtoAndIban()
    {
        System.out.println(accountPatchDTO.getStatus());
        System.out.println(accountPatchDTO.getAbsoluteLimit());

        //Special setup for the rest template to allow PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", accountPatchDTO.getAbsoluteLimit());
        map.put("status", accountPatchDTO.getStatus());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, employeeHeaders);

        testRestTemplate.exchange("/api/accounts/" + expectedAccounts.get(0).getAccount_id(), HttpMethod.PATCH, entity, String.class);

    }

    @Then("^the updated account is in the database$")
    public void thenItIsInTheDatabase() {
        actualAccounts.addAll(accountRepository.findAll());
    }

    @And("^it has the updated properties$")
    public void andItHasUpdatedProperties() {
        //Checks against the original account to make sure the values are not equal
        Assertions.assertNotEquals(expectedAccounts.get(0).getAbsoluteLimit(), actualAccounts.get(0).getAbsoluteLimit());
        Assertions.assertNotEquals(expectedAccounts.get(0).getStatus(), actualAccounts.get(0).getStatus());

        //Checks against the dto to check that the values are equal
        Assertions.assertEquals(accountPatchDTO.getAbsoluteLimit(), actualAccounts.get(0).getAbsoluteLimit());
        Assertions.assertEquals(accountPatchDTO.getStatus(), actualAccounts.get(0).getStatus());
    }

}
