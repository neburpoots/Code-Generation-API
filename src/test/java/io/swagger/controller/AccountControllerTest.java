package io.swagger.controller;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {Swagger2SpringBoot.class, AccountService.class, AccountController.class})
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AccountController accountController;


    @Autowired
    private AccountRepository accountRepo;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void createAccountSuccesfullyShouldReturn201() throws Exception {

//        Mockito.when(accountController.createAccount(
//                new AccountPostDTO()
//                        .absoluteLimit(new BigDecimal(-4500))
//                        .accountType(AccountType.PRIMARY)
//                        .user_id(UUID.fromString("f963d334-2a06-4e75-96d9-16cbb8b0c2b3"))))
//                .thenReturn(new ResponseEntity<AccountGetDTO>(this.modelMapper.map(createMockAccount(), AccountGetDTO.class), HttpStatus.OK));
//
        String absolute_limit = "-4500";
        String account_type = "PRIMARY";
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";
////
        String body = "{\"absolute_limit\":" + absolute_limit + ", \"account_type\":\""
                + account_type + "\"user_id\":\"" + uuid + "\"}";

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated()).andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);

    }

    @Test
    public void createAccountWithoutJWTTOKENShouldReturn403() throws Exception {

        String absolute_limit = "-10000";
        String account_type = "SAVINGS";
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";
//
        String body = "{\"absolute_limit\":" + absolute_limit + ", \"account_type\":\""
                + account_type + "\"user_id\":\"" + uuid + "\"}";

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden()).andReturn();

        String resultDOW = result.getResponse().getContentAsString();
        assertNotNull(resultDOW);

    }

}