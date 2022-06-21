package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;



@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    private ModelMapper modelMapper;

    //jwt token for employee
    private HttpHeaders headers;

    //jwt token for customer
    private HttpHeaders customerHeaders;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestRestTemplate testRestTemplate;


    private final String url = "http://localhost:8080/api/accounts";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();
        InitHeaderTokens();
    }

    //Creates header tokens for requests
    private void InitHeaderTokens() {

        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "CUSTOMER"));

        User customerUser = userRepository.findByEmail("customer@student.inholland.nl");

        customerHeaders = new HttpHeaders();
        customerHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        customerHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("customer@student.inholland.nl", roles, customerUser.getUser_id()));

        roles.add(new Role(2, "EMPLOYEE"));

        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", roles, UUID.randomUUID()));
    }

    private void validateAccount(final Account expected, final Account actual) {
        Assertions.assertEquals(expected.getBalance(), actual.getBalance());
        Assertions.assertEquals(expected.getAccount_id(), actual.getAccount_id());
        Assertions.assertEquals(expected.getAbsoluteLimit(), actual.getAbsoluteLimit());
    }

    @Test
    public void getAllAccountsSuccessfullyChecksListShouldReturn200() throws IOException {
        getALlAccountsSuccesfullyWithGivenUrlAndChecksEntirePage(0, 5,"/api/accounts");
    }

    @Test
    public void getAllAccountsSuccessfullyWithPaginationParametersReturns200() throws IOException {
        getALlAccountsSuccesfullyWithGivenUrlAndChecksEntirePage(0, 5,"/api/accounts?page=0&size=5");
    }

    public void getALlAccountsSuccesfullyWithGivenUrlAndChecksEntirePage(Integer page, Integer size, String url) throws IOException {

        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> expectedAccounts = accountRepository.findByAccountTypeIsNot(AccountType.BANK, pageable);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<RestPageImpl<Account>> accounts =

                testRestTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        Assertions.assertEquals(accounts.getStatusCodeValue(), 200);

        List<Account> actualAccounts = accounts.getBody().getContent();

        Assertions.assertEquals(accounts.getBody().getTotalElements(), expectedAccounts.getTotalElements());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.getContent().get(index), actualAccounts.get(index)));
    }

    @Test
    public void getAllAccountsWithIncorrectPaginationAndSizeShouldThrow400() throws IOException {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts?page=-10&size=-10", HttpMethod.GET, request, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Invalid page or page size");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);
    }

    @Test
    public void getAllAccountsWithCustomerJWTShouldReturn403() throws IOException {

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.GET, request, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "You are not authorized to make this request.");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void getAllAccountsWithoutAValidJWTTokenShouldThrow403() throws IOException {

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.GET, null, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Access Denied");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void getAllAccountsWithUserParameterShouldReturn200() throws IOException {

        User user = userRepository.findByEmail("ruben@student.inholland.nl");

        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Pageable pageable = PageRequest.of(0, 5);
        Page<Account> expectedAccounts = accountRepository.findByUserAndAccountTypeIsNot(user, AccountType.BANK, pageable);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<RestPageImpl<Account>> accounts =
                testRestTemplate.exchange("/api/accounts?user_id=" + user.getUser_id(), HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        Assertions.assertEquals(accounts.getStatusCodeValue(), 200);

        List<Account> actualAccounts = accounts.getBody().getContent();

        Assertions.assertEquals(accounts.getBody().getTotalElements(), expectedAccounts.getTotalElements());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.getContent().get(index), actualAccounts.get(index)));
    }

    @Test
    public void getAllAccountsWithUserParameterAndUserJWTShouldReturn200() throws IOException {

        User user = userRepository.findByEmail("customer@student.inholland.nl");

        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Pageable pageable = PageRequest.of(0, 5);
        Page<Account> expectedAccounts = accountRepository.findByUserAndAccountTypeIsNot(user, AccountType.BANK, pageable);

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<RestPageImpl<Account>> accounts =
                testRestTemplate.exchange("/api/accounts?user_id=" + user.getUser_id(), HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        Assertions.assertEquals(accounts.getStatusCodeValue(), 200);

        List<Account> actualAccounts = accounts.getBody().getContent();

        Assertions.assertEquals(accounts.getBody().getTotalElements(), expectedAccounts.getTotalElements());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.getContent().get(index), actualAccounts.get(index)));
    }

    @Test
    public void getAllAccountsWithWrongUserParameterAndUserJWTShouldReturn403() throws IOException {

        User user = userRepository.findByEmail("ruben@student.inholland.nl");

        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Pageable pageable = PageRequest.of(0, 5);
        Page<Account> expectedAccounts = accountRepository.findByUserAndAccountTypeIsNot(user, AccountType.BANK, pageable);

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts?user_id=" + user.getUser_id(), HttpMethod.GET, request, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "You are not authorized to make this request.");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void getAllAccountsWithNonExistsUserShouldThrow400() throws IOException {

        HttpEntity request = new HttpEntity(headers);

        String uuid = UUID.randomUUID().toString();

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts?user_id=" + uuid, HttpMethod.GET, request, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "User with id: '" + uuid + "' not found");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),404);
    }

    @Test
    public void getAllAccountsWithCustomerAccountsShouldReturn403() throws IOException {

        HttpEntity request = new HttpEntity(customerHeaders);


        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.GET, request, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "You are not authorized to make this request.");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void getAllAccountsWithTypeParameterShouldReturn200() throws IOException {

        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Pageable pageable = PageRequest.of(0, 5);
        Page<Account> expectedAccounts = accountRepository.findByAccountType(AccountType.PRIMARY, pageable);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<RestPageImpl<Account>> accounts =
                testRestTemplate.exchange("/api/accounts?type=PRIMARY", HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        Assertions.assertEquals(accounts.getStatusCodeValue(), 200);

        List<Account> actualAccounts = accounts.getBody().getContent();

        Assertions.assertEquals(accounts.getBody().getTotalElements(), expectedAccounts.getTotalElements());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.getContent().get(index), actualAccounts.get(index)));
    }

    @Test
    public void getAllAccountsWithAccountTypeAndUserShouldReturn400() throws IOException {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts?type=WRONGTYPE", HttpMethod.GET, request, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Filter type is incorrect.");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);
    }

    @Test
    public void getAllAccountsWithAccountTypeAndUserShouldReturn200() throws IOException {

        User user = userRepository.findByEmail("ruben@student.inholland.nl");

        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Pageable pageable = PageRequest.of(0, 5);
        Page<Account> expectedAccounts = accountRepository.findByUserAndAccountType(user, AccountType.PRIMARY, pageable);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<RestPageImpl<Account>> accounts =
                testRestTemplate.exchange("/api/accounts?type=PRIMARY&user_id=" + user.getUser_id(), HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        Assertions.assertEquals(accounts.getStatusCodeValue(), 200);

        List<Account> actualAccounts = accounts.getBody().getContent();

        Assertions.assertEquals(accounts.getBody().getTotalElements(), expectedAccounts.getTotalElements());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.getContent().get(index), actualAccounts.get(index)));
    }

    @Test
    public void getAllAccountsWithAccountTypeAndUserAndSizeAndPageShouldReturn200() throws IOException {

        User user = userRepository.findByEmail("ruben@student.inholland.nl");

        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Pageable pageable = PageRequest.of(0, 5);
        Page<Account> expectedAccounts = accountRepository.findByUserAndAccountType(user, AccountType.PRIMARY, pageable);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<RestPageImpl<Account>> accounts =
                testRestTemplate.exchange("/api/accounts?type=PRIMARY&page=0&size=5&user_id=" + user.getUser_id(), HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<Account>>() {
                });

        Assertions.assertEquals(accounts.getStatusCodeValue(), 200);

        List<Account> actualAccounts = accounts.getBody().getContent();

        Assertions.assertEquals(accounts.getBody().getTotalElements(), expectedAccounts.getTotalElements());
        IntStream.range(0, actualAccounts.size())
                .forEach(index -> validateAccount(expectedAccounts.getContent().get(index), actualAccounts.get(index)));
    }

    @Test
    public void getOneAccountSuccesfullyShouldReturn200() throws IOException {
        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Account testAccount = accountRepository.findAll().get(2);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Account> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.GET, request, new ParameterizedTypeReference<Account>() {
                });

        Assertions.assertEquals(account.getStatusCodeValue(), 200);

        Account actualAccount = account.getBody();

        validateAccount(testAccount, actualAccount);
    }

    @Test
    public void getOneAccountSuccesfullyWithUserJWTOwnAccountShouldReturn200() throws IOException {
        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        User user = userRepository.findByEmail("customer@student.inholland.nl");
        List<Account> testAccounts = accountRepository.findByUser(user);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Account> account =
                testRestTemplate.exchange("/api/accounts/" + testAccounts.get(0).getAccount_id(), HttpMethod.GET, request, new ParameterizedTypeReference<Account>() {
                });

        Assertions.assertEquals(account.getStatusCodeValue(), 200);

        Account actualAccount = account.getBody();

        validateAccount(testAccounts.get(0), actualAccount);
    }

    @Test
    public void getOneAccountWithIncorrectIbanShouldReturn404() throws IOException {
        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + "NOTANIBAN123", HttpMethod.GET, request, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Could not find account");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),404);
    }

    @Test
    public void getOneAccountWithNoHeadersShouldReturn403() throws IOException {
        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Account testAccount = accountRepository.findAll().get(2);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.GET, null, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Access Denied");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void getOneAccountThatDoesNotBelongToCustomerWithCustomerJWTShouldReturn403() throws IOException {
        //EXPECTED RETURN RESULT TO COMPARE
        // request body parameters
        Account testAccount = accountRepository.findAll().get(2);

        HttpEntity request = new HttpEntity(customerHeaders);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.GET, null, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Access Denied");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void createAccountSuccesfullyChecksAllPropertiesShouldReturn201Created() throws IOException {
        User user = userRepository.findByEmail("noaccount@student.inholland.nl");
        Long accountRepoCountBefore = accountRepository.count();

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        AccountGetDTO mappedtoAccountGetDTO = objectMapper.readValue(account.getBody(), AccountGetDTO.class);


        Assertions.assertEquals(account.getStatusCodeValue(),201);

        Assertions.assertNotNull(mappedtoAccountGetDTO.getAccount_id());
        Assertions.assertNotNull(mappedtoAccountGetDTO.getAccountType());
        Assertions.assertNotNull(mappedtoAccountGetDTO.getAbsoluteLimit());
        Assertions.assertNotNull(mappedtoAccountGetDTO.getBalance());
        Assertions.assertNotNull(mappedtoAccountGetDTO.getuser());

        Long accountRepoCountAfter = accountRepository.count();

        //Checks if the repository has the same number of items
        System.out.println(accountRepoCountBefore);
        System.out.println(accountRepoCountAfter);
        Assertions.assertNotEquals(accountRepoCountBefore,accountRepoCountAfter);

    }

    @Test
    public void createAccountWithWrongAbsoluteLimitShouldReturn400() throws IOException {
        User user = userRepository.findByEmail("noaccount@student.inholland.nl");

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -20000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);

    }

    @Test
    public void createAccountWithCustomerJWTShouldReturn403() throws IOException {
        User user = userRepository.findByEmail("noaccount@student.inholland.nl");

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, customerHeaders);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Forbidden");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);

    }

    @Test
    public void createAccountWithWrongAccountType() throws IOException {
        User user = userRepository.findByEmail("noaccount@student.inholland.nl");

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -20000);
        map.put("account_type", "WRONGTYPE");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Provided json object was invalid. ");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);

    }

    @Test
    public void createAccountWithWrongHTTPMethodShouldReturn405() throws IOException {
        User user = userRepository.findByEmail("noaccount@student.inholland.nl");

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "PRIMARY");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.PUT, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Request method 'PUT' not supported");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),405);

    }

    @Test
    public void createAccountWithNoJWTTokenShouldReturn403() throws IOException {
        User user = userRepository.findByEmail("noaccount@student.inholland.nl");

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(), "Access Denied");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void createAccountWithANonExistentUserShouldReturn404UserNotFound() throws IOException {

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", UUID.randomUUID());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),404);

    }

    @Test
    public void createAccountWithBankAccountUserShouldNotBeAllowedThrows400() throws IOException {

        User user = userRepository.findByEmail("bankaccount@bankaccount.nl");

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"You can't create a account for the bank user");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);

    }

    @Test
    public void createAccountWithUserThatHasBothAccountsThrows409() throws IOException {

        User user = userRepository.findByEmail("ruben@student.inholland.nl");

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", user.getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"Customer already has a primary and savings account");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),409);

    }

    @Test
    public void editAccountSuccesfullyShouldReturn200() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(2);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -4999);
        map.put("status", "false");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.PATCH, entity, String.class);

        AccountGetDTO mappedtoAccountGetDTO = objectMapper.readValue(account.getBody(), AccountGetDTO.class);


        Assertions.assertEquals(account.getStatusCodeValue(),200);

        Assertions.assertNotNull(mappedtoAccountGetDTO.getAccount_id());
        Assertions.assertNotEquals(mappedtoAccountGetDTO.getAbsoluteLimit(),testAccount.getAbsoluteLimit());
        Assertions.assertNotEquals(mappedtoAccountGetDTO.getStatus(), testAccount.getStatus());

    }

    @Test
    public void editAccountWithIncorrectStatusThrows400() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(2);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -4999);
        map.put("status", "notABoolean");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.PATCH, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"Provided json object was invalid. ");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);

    }

    @Test
    public void editAccountWithWrongHTTPMethodShouldReturn405() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(2);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -4999);
        map.put("status", "notABoolean");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"Request method 'POST' not supported");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),405);
    }

    @Test
    public void editAccountWithIncorrectAbsoluteLimitThrows400() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(2);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", 15000);
        map.put("status", "true");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.PATCH, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"[must be less than 0.01]");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);
    }

    @Test
    public void editAccountWithIncorrectAccountThrows404() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -5000);
        map.put("status", "true");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + "NOTAUSER", HttpMethod.PATCH, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"Could not find account");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),404);

    }

    @Test
    public void editAccountWithCustomerJWTShouldReturn403() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(2);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -5000);
        map.put("status", "true");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, customerHeaders);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.PATCH, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"Forbidden");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());
        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }



    @Test
    public void editAccountWithoutJWTTokenReturns403() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(2);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -5000);
        map.put("status", "true");


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.PATCH, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"Access Denied");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);

    }

    @Test
    public void editAccountWithoutUpdatingAnythingReturns422() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(2);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", testAccount.getAbsoluteLimit());
        map.put("status", testAccount.getStatus());


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.PATCH, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"Nothing is updated");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),422);

    }

    @Test
    public void editAccountWithTypeBankShouldThrow400() throws IOException {

        //Necessary for testresttemplate to use PATCH requests
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Account testAccount = accountRepository.findAll().get(0);

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", testAccount.getAbsoluteLimit());
        map.put("status", testAccount.getStatus());


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts/" + testAccount.getAccount_id(), HttpMethod.PATCH, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        System.out.println(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertEquals(mappedToErrorMessage.getMessage(),"You do not have access to this account");
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),400);

    }







}