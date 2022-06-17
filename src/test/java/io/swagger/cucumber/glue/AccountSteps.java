package io.swagger.cucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class AccountSteps {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Account> expectedAccounts;

    private List<Account> actualAccounts;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders headers;

    @Before
    public void setup()
    {
        expectedAccounts = new ArrayList<>();
        actualAccounts = new ArrayList<>();
        accountRepository.deleteAll();
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "EMPLOYEE"));
        roles.add(new Role(2, "CUSTOMER"));


        headers.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", roles, UUID.randomUUID()));
    }

    @Given("^the following accounts$")
    public void givenTheFollowingAccounts(final List<Account> accounts)
    {
        expectedAccounts.addAll(accounts);

        //Set the users for testing
        List<User> testUser = userRepository.findAll();

        expectedAccounts.forEach(c -> c.setUser(testUser.get(0)));

        //Might need users
        accountRepository.saveAll(accounts);
    }

    @When("^the user requests all the accounts")
    public void whenTheUserRequestAllTheAccounts() throws IOException {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<RestPageImpl<Account>> accounts =

                        testRestTemplate.exchange("/api/accounts", HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                        });

        actualAccounts = accounts.getBody().getContent();

    }

    @When("^a employee posts a new account with absolute limit ([0-9]) for customer (.*)$")
    public void whenAEmployeeCreatesAnAccount(final Integer absoluteLimit, final String email) {
        User user = userRepository.findByEmail(email);

        HttpEntity request = new HttpEntity(headers);

        Account newAccount = new Account(new BigDecimal(0), new BigDecimal(absoluteLimit), AccountType.PRIMARY, true);

        expectedAccounts.add(newAccount);

        AccountPostDTO accountPostDTO = objectMapper.convertValue(newAccount, AccountPostDTO.class);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, request, String.class);

    }

    @Then("^all the accounts are returned")
    public void thenAllTheAccountsAreReturned() {
        validateAccounts();
    }

    @Then("^it is in the database$")
    public void thenItisInTheDatabase() {
        actualAccounts.addAll(accountRepository.findAll());
        validateAccounts();
    }

    @And("^it has an id$")
    public void andItHasAnId() {
        Assertions.assertNotNull(actualAccounts.get(0).getAccount_id());
    }

    private void validateAccounts() {
        Assertions.assertEquals(expectedAccounts.size(), actualAccounts.size());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.get(index), actualAccounts.get(index)));
    }

    private void validateAccount(final Account expected, final Account actual) {
        Assertions.assertEquals(expected.getBalance(), actual.getBalance());
        Assertions.assertEquals(expected.getAbsoluteLimit(), actual.getAbsoluteLimit());
    }
}
