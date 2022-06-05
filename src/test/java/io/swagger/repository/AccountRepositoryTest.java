package io.swagger.repository;

import io.swagger.controller.AccountController;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {AccountService.class, AccountController.class})
@WebMvcTest
public class AccountRepositoryTest {

    private AccountService accountService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountController accountController;
    @MockBean
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
    void createMockAccount() {
        User mockUser = new User();
        mockUser.setUser_id(UUID.randomUUID());
        Account account = new Account();
        System.out.println(account.getAccount_id());
        account.setUser(mockUser);
        account.setStatus(true);
        account.setBalance(new BigDecimal(0));
        account.setAbsoluteLimit(new BigDecimal(-4500));
        account.setAccountType(AccountType.PRIMARY);

        accountRepo.save(account);
    }
}
