package io.swagger.controller;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.Swagger2SpringBoot;
import io.swagger.cucumber.CucumberIT;
import io.swagger.exception.BadRequestException;
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
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.utils.RestPageImpl;
import org.hamcrest.core.IsNull;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    private HttpHeaders headers;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestRestTemplate testRestTemplate;


    private String url = "http://localhost:8080/api/accounts";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();
        InitHeaderTokens();
    }

    //Creates header tokens for requests
    private void InitHeaderTokens() {
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "EMPLOYEE"));
        roles.add(new Role(2, "CUSTOMER"));

        headers.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", roles, UUID.randomUUID()));

    }

    @Test
    public void createAccountWithWrongAbsoluteLimitShouldReturn400() {
        List<User> users = userRepository.findAll();

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -20000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", users.get(users.size() - 1).getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AccountGetDTO> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, AccountGetDTO.class);

        System.out.println(account);
        Assertions.assertEquals(account.getStatusCodeValue(),400);

    }

    @Test
    public void createAccountWithWrongAccountType() {
        List<User> users = userRepository.findAll();

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -20000);
        map.put("account_type", "WRONGTYPE");
        map.put("user_id", users.get(users.size() - 1).getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<AccountGetDTO> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, AccountGetDTO.class);

        System.out.println(account);
        Assertions.assertEquals(account.getStatusCodeValue(),400);

    }

    @Test
    public void createAccountSuccesfullyChecksAllPropertiesShouldReturn201Created() throws IOException {
        List<User> users = userRepository.findAll();
        Long accountRepoCountBefore = accountRepository.count();

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", users.get(users.size() - 1).getUser_id());

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
    public void createAccountWithNoJWTTokenShouldReturn403() throws IOException {
        List<User> users = userRepository.findAll();

        // request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("absolute_limit", -10000);
        map.put("account_type", "SAVINGS");
        map.put("user_id", users.get(users.size() - 1).getUser_id());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map);

        ResponseEntity<String> account =
                testRestTemplate.exchange("/api/accounts", HttpMethod.POST, entity, String.class);

        //Converts String to object
        ErrorMessage mappedToErrorMessage = objectMapper.readValue(account.getBody(), ErrorMessage.class);

        Assertions.assertNotNull(mappedToErrorMessage.getMessage());
        Assertions.assertNotNull(mappedToErrorMessage.getTimestamp());

        Assertions.assertEquals(account.getStatusCodeValue(),403);
    }

    @Test
    public void createAccountWithANonExistentUserShouldReturn404UserNotFound() throws IOException {
        List<User> users = userRepository.findAll();
        Long accountRepoCountBefore = accountRepository.count();

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


}