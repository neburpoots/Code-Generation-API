package io.swagger.cucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

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

    @Before
    public void setup()
    {
        expectedAccounts = new ArrayList<>();
        actualAccounts = new ArrayList<>();
        accountRepository.deleteAll();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
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

    private HttpHeaders createHttpHeaders(String user, String password)
    {
        String notEncoded = user + ":" + password;
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(notEncoded.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", encodedAuth);
        return headers;
    }


    @When("^the user requests all the accounts")
    public void whenTheUserRequestAllTheAccounts() throws IOException {
        actualAccounts.addAll(Arrays.asList(
                objectMapper.readValue(
                        testRestTemplate.getForEntity("/api/accounts", String.class)
                                .getBody(), Account[].class)));
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
