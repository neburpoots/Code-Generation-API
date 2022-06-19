package io.swagger.cucumber.glue.accountsteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.Assert.assertThat;

public abstract class BaseAccountSteps {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    //Checking 2** codes
    protected List<Account> expectedAccounts;
    protected List<Account> actualAccounts;

    //Checking error messages
    protected String expectedErrorMessage;
    //Checking error codes
    protected Integer expectedCode;

    //Expected error message
    protected ErrorMessage actualErrorMessage;


    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    //Bearer token for employee
    protected HttpHeaders employeeHeaders;

    //Bearer token for customer
    protected HttpHeaders customerHeaders;

//    @Before("@accounts")
    public void setup()
    {
        expectedAccounts = new ArrayList<>();
        actualAccounts = new ArrayList<>();
        accountRepository.deleteAll();
        setupHeaders();
    }

    private void setupHeaders()
    {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(2, "CUSTOMER"));

        setupCustomerHeader(roles);
        setupEmployeeHeader(roles);
    }

    private void setupCustomerHeader(List<Role> roles)
    {
        customerHeaders = new HttpHeaders();
        customerHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        User customerUser = userRepository.findByEmail("customer@student.inholland.nl");

        customerHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", roles, customerUser.getUser_id()));
    }

    private void setupEmployeeHeader(List<Role> roles)
    {
        employeeHeaders = new HttpHeaders();
        employeeHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        roles.add(new Role(1, "EMPLOYEE"));

        employeeHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", roles, UUID.randomUUID()));
    }

    protected void validateAccounts() {
        Assertions.assertEquals(expectedAccounts.size(), actualAccounts.size());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.get(index), actualAccounts.get(index)));
    }

    protected void validateAccount(final Account expected, final Account actual) {
        assertThat(expected.getBalance(),  Matchers.comparesEqualTo(actual.getBalance()));
        assertThat(expected.getAbsoluteLimit(),  Matchers.comparesEqualTo(actual.getAbsoluteLimit()));
        Assertions.assertEquals(expected.getStatus(),  actual.getStatus());
        Assertions.assertEquals(expected.getAccountType(),  actual.getAccountType());
    }

    protected void validateErrorMessageWithoutStatusCodeParam() {
        validateErrorMessage();
        Assertions.assertEquals(expectedCode, (Integer) actualErrorMessage.getStatusCode());
    }

    private void validateErrorMessage() {
        Assertions.assertNotNull(actualErrorMessage.getMessage());
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage.getMessage());
        Assertions.assertNotNull(actualErrorMessage.getTimestamp());
    }

    protected void validateErrorMessageWithStatusCode(Integer statusCode) {
        validateErrorMessage();
        Assertions.assertEquals(expectedCode, statusCode);
    }
}
