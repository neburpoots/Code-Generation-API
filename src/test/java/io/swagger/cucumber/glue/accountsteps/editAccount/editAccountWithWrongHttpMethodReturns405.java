package io.swagger.cucumber.glue.accountsteps.editAccount;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.cucumber.glue.accountsteps.BaseAccountSteps;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.account.AccountPatchDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class editAccountWithWrongHttpMethodReturns405 extends BaseAccountSteps {

    private AccountPatchDTO accountPatchDTO;
    private Integer statusCode;

    @Before("@editAccountWithWrongHttpMethodReturns405")
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

    @Given("^the valid dto for the wrong http request$")
    public void givenTheValidDtoForTheWrongHttpRequest(final List<Account> accounts)
    {
        this.accountPatchDTO = new AccountPatchDTO()
                .absoluteLimit(accounts.get(0).getAbsoluteLimit())
                .status(accounts.get(0).getStatus());
    }

    @When("^a employee makes a post request to the patch endpoint$")
    public void whenAEmployeeTriesToPatchAccountWithInvalidDTO() throws IOException {

        System.out.println(accountPatchDTO.getStatus());
        System.out.println(accountPatchDTO.getAbsoluteLimit());

        //Special setup for the rest template to allow PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", accountPatchDTO.getAbsoluteLimit());
        map.put("status", accountPatchDTO.getStatus());

        //Entity with customer jwt to cause 403
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, employeeHeaders);

        ResponseEntity<String> error =
                testRestTemplate.exchange("/api/accounts/" + expectedAccounts.get(0).getAccount_id(), HttpMethod.POST, entity, String.class);

        //Converts String to object
        this.actualErrorMessage = objectMapper.readValue(error.getBody(), ErrorMessage.class);
        this.statusCode = error.getStatusCodeValue();
    }

    @Then("a {int} method not allowed should be returned and the following error message given: {string}")
    public void then405MethodNotAllowedIsReturnedWithErrorMessage(final Integer code, final String message) {
        this.expectedCode = code;
        this.expectedErrorMessage = message;

        validateErrorMessageWithStatusCode(statusCode);
    }



    @And("the existing account has not been updated in the database")
    public void andTheAccountHasNotBeenAddedToTheDatabase() {

        this.actualAccounts.addAll(accountRepository.findAll());

        //Validates that nothing has been updated
        validateAccounts();
    }
}
