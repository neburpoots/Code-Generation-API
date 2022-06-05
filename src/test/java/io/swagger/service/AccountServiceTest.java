package io.swagger.service;

import io.swagger.controller.AccountController;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.account.AccountPostDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserLoginReturnDTO;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountServiceTest {


    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

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
        accountService = new AccountService(accountRepo, userService,authenticationManager, jwtTokenProvider);
    }

//    @Test
//    @DisplayName("getAccountWhichDoesNotExistShouldReturn404")
//    public void getAccountWhichDoesNotExistShouldReturn404() {
//        AccountService accountService = new AccountService(null,null,null,null);
//        accountService.getAccount("NL9999999999", HttpServletRequest req);
//    }
//
    @Test
    @DisplayName("getAccountWhichDoesNotExistShouldReturn404")
    public void getAccountWhichDoesNotExistShouldReturn404() {

    }

    private Account createMockAccount() {
        User mockUser = new User();
        mockUser.setUser_id(UUID.randomUUID());
        Account account = new Account();
        System.out.println(account.getAccount_id());
        account.setUser(mockUser);
        account.setStatus(true);
        account.setBalance(new BigDecimal(0));
        account.setAbsoluteLimit(new BigDecimal(-4500));
        account.setAccountType(AccountType.PRIMARY);

        return account;
    }

    @Test
    public void createAccountSuccesfullyShouldReturn201() throws Exception {



        Mockito.when(accountService.createAccount(
                new AccountPostDTO()
                        .absoluteLimit(new BigDecimal(-4500))
                        .accountType(AccountType.PRIMARY)
                        .user_id(UUID.randomUUID()))).thenReturn(this.modelMapper.map(createMockAccount(), AccountGetDTO.class));

//
//
//        String email = "ruben@student.inholland.nl";
//        String password = "Secret123!";
////
//        String body = "{\"email\":\"" + email + "\", \"password\":\""
//                + password + "\"}";
////        //Initial login request
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isOk()).andReturn();

    }
}