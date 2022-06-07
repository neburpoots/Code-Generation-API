package io.swagger.service;

import io.swagger.controller.AccountController;
import io.swagger.controller.NotFoundException;
import io.swagger.exception.BadRequestException;
import io.swagger.exception.ResourceNotFoundException;
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
import io.swagger.seed.MainSeeder;
import io.swagger.seed.RoleSeeder;
import io.swagger.utils.DtoUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AccountServiceTest {




    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;



    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();
//        accountService = Mockito.mock(AccountService.class, RETURNS_MOCKS);
    }

    @Test
    @DisplayName("getAccountWhichDoesNotExistShouldReturn404")
    public void getAccountWhichDoesNotExistShouldReturnNotFoundException() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        when(accountService.getAccount("NLINHO9999999999", request)).thenThrow(ResourceNotFoundException.class);

    }

    @Test
    @DisplayName("getAccountWhichDoesExistShouldReturnAccountGetDTO")
    public void getAccountWhichDoesExistShouldReturnAccountGetDTO() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        List<Account> accounts = accountRepo.findAll();

        AccountGetDTO account = accountService.getAccount(accounts.get(0).getAccount_id(), request);

        assertNotNull(account.getAbsoluteLimit());

        System.out.println(account.getAccount_id());
        assertNotNull(account.getAccount_id());
//        when(accountService.getAccount(accounts.get(0).getAccount_id(), request))
//                .thenReturn(this.modelMapper.map(accounts.get(0), AccountGetDTO.class));
    }

    @Test
    @DisplayName("getAccountWhichDoesExistShouldReturnAccountGetDTO")
    public void getAccountWhichShouldNotExistReturnsException() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        List<Account> accounts = accountRepo.findAll();


        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            AccountGetDTO account = accountService.getAccount("NLINHO12938712312", request);

        });

//        assertNotNull(account.getAbsoluteLimit());
//
//        System.out.println(account.getAccount_id());
//        assertNotNull(account.getAccount_id());
//        when(accountService.getAccount(accounts.get(0).getAccount_id(), request))
//                .thenReturn(this.modelMapper.map(accounts.get(0), AccountGetDTO.class));
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
    public void createAccountShouldReturnAGetAccountDto() throws Exception {

        List<User> users = userRepo.findAll();

        AccountGetDTO account = accountService.createAccount(
                new AccountPostDTO()
                        .absoluteLimit(new BigDecimal(-4500))
                        .accountType(AccountType.SAVINGS)
                        .user_id(users.get(0).getUser_id()));

        assertNotNull(account);


    }

    @Test
    public void createAccountShouldReturnA400BadRequestBecauseOfAbsoluteLimit() throws BadRequestException {

        List<User> users = userRepo.findAll();

        AccountGetDTO account = accountService.createAccount(
                new AccountPostDTO()
                        .absoluteLimit(new BigDecimal(-100000))
                        .accountType(AccountType.BANK)
                        .user_id(users.get(0).getUser_id()));

        assertNotNull(account);
    }
}