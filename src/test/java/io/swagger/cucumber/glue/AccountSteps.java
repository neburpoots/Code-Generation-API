package io.swagger.cucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.IOException;
import java.util.*;
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

    @Then("^all the accounts are returned")
    public void thenAllTheAccountsAreReturned() {
        validateAccounts();
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
