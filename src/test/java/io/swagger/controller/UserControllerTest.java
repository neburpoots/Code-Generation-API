package io.swagger.controller;

import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserLoginReturnDTO;
import io.swagger.model.user.UserSearchDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.RefreshTokenService;
import io.swagger.service.UserService;
import io.swagger.utils.DtoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserService.class, UserController.class})
@WebMvcTest
public class UserControllerTest {

    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @MockBean
    private UserRepository userRepo;
    @MockBean
    private RoleRepository roleRepo;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private DtoUtils dtoUtils;
    @MockBean
    private RefreshTokenService refreshTokenService;

    @Autowired
    private HttpServletRequest request;
    private List<Role> roles;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.userService = new UserService(userRepo, roleRepo, authenticationManager, jwtTokenProvider, passwordEncoder, dtoUtils, refreshTokenService);
    }

    @Test
    public void registerUserShouldReturnStatusCreated() throws Exception {
        String username = "ruben@student.inholland.nl";
        String password = "Secret123!";
        String body = "{\"username\":\"" + username + "\", \"password\":\""
                + password + "\"}";

        //Initial login request
        this.mockMvc.perform(post("http://localhost:8080/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()).andReturn();
    }

    private User makeCustomer() {
        User customer = new User();
        customer.setUser_id(UUID.fromString("69e0d255-94e9-4e8b-84eb-fbed9e1a007b"));
        customer.setEmail("customer@student.inholland.nl");
        customer.setLastname("Mr");
        customer.setFirstname("Customer");
        customer.setPassword("Secret123!");
        customer.setDailyLimit(new BigDecimal(50000));
        customer.setTransactionLimit(new BigDecimal(4000));
        customer.setRolesForUser(roles);

        return customer;
    }

    private User makeEmployee() {
        User employee = new User();
        employee.setUser_id(UUID.fromString("f963d334-2a06-4e75-96d9-16cbb8b0c2b3"));
        employee.setEmail("employee@student.inholland.nl");
        employee.setLastname("Mr");
        employee.setFirstname("Employee");
        employee.setPassword("Secret123!");
        employee.setDailyLimit(new BigDecimal(50000));
        employee.setTransactionLimit(new BigDecimal(4000));
        employee.setRolesForUser(List.of(roleRepo.findById(1).orElse(null), roleRepo.findById(2).orElse(null)));

        return employee;
    }


    @Test
    public void testLoginUserShouldReturnStatus200() throws Exception {
        when(userController.loginUser(
                new UserLoginDTO()
                        .password("Secret123!")
                        .email("ruben@student.inholland.nl")))
                .thenReturn(new ResponseEntity<UserLoginReturnDTO>(new UserLoginReturnDTO()
                        .user_id(UUID.randomUUID())
                        .accessToken("flajsdfoiaewfase.fasjeofjasefjoas.afejoasfjaesi")
                        .refreshToken("fjaoesfjasef.aefjasiofjioasf.asjifoasjefoie")
                        .email("ruben@student.inholland.nl"), HttpStatus.OK)
                );
        String username = "ruben@student.inholland.nl";
        String password = "Secret123!";
        String body = "{\"username\":\"" + username + "\", \"password\":\""
                + password + "\"}";
        //Initial login request
        this.mockMvc.perform(post("http://localhost:8080/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void getUserWithRoleCustomerShouldReturnStatusOKAndOneObject() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";

        when(userController.getUserById(uuid))
                .thenReturn(new ResponseEntity<DTOEntity>(new UserSearchDTO()
                        .user_id(UUID.fromString(uuid))
                        .firstname("Ruben")
                        .lastname("Stoop")
                        .email("ruben@student.inholland.nl"), HttpStatus.OK)
                );
        mockMvc.perform(get("http://localhost:8080/api/users/f963d334-2a06-4e75-96d9-16cbb8b0c2b3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value("f963d334-2a06-4e75-96d9-16cbb8b0c2b3"))
                .andExpect(jsonPath("$.firstname").value("Ruben"))
                .andExpect(jsonPath("$.lastname").value("Stoop"))
                .andExpect(jsonPath("$.email").value("ruben@student.inholland.nl"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void getUserWithRoleEmployeeShouldReturnStatusOKAndOneObject() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";

        when(userController.getUserById(uuid))
                .thenReturn(new ResponseEntity<DTOEntity>(new UserGetDTO()
                        .user_id(UUID.fromString(uuid))
                        .firstname("Ruben")
                        .lastname("Stoop")
                        .email("ruben@student.inholland.nl")
                        .transactionLimit(new BigDecimal(50))
                        .dailyLimit(new BigDecimal(2500))
                        .role(List.of(new Role(1, "ROLE_CUSTOMER"))), HttpStatus.OK)
                );
        mockMvc.perform(get("http://localhost:8080/api/users/f963d334-2a06-4e75-96d9-16cbb8b0c2b3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value("f963d334-2a06-4e75-96d9-16cbb8b0c2b3"))
                .andExpect(jsonPath("$.firstname").value("Ruben"))
                .andExpect(jsonPath("$.lastname").value("Stoop"))
                .andExpect(jsonPath("$.email").value("ruben@student.inholland.nl"))
                .andExpect(jsonPath("$.transaction_limit").value("50"))
                .andExpect(jsonPath("$.daily_limit").value("2500"))
                .andExpect(jsonPath("$.role[0].role_id").value("1"))
                .andExpect(jsonPath("$.role[0].name").value("ROLE_CUSTOMER"))
                .andExpect(jsonPath("$.role[0].authority").value("ROLE_CUSTOMER"));
    }
}