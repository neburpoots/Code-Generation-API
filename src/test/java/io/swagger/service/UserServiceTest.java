package io.swagger.service;

import io.swagger.controller.UserController;
import io.swagger.model.user.UserGetDTO;
import io.swagger.model.user.UserPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.utils.DtoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserService.class, UserController.class})
@WebMvcTest
class UserServiceTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;
    @MockBean
    private RoleRepository roleRepo;

    @Autowired
    private UserController userController;

    @MockBean
    @Autowired
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private DtoUtils dtoUtils;
    @MockBean
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private UserGetDTO createMockUser() {
        return new UserGetDTO()
                .user_id(UUID.randomUUID())
                .email("ruben@student.inholland.nl")
                .firstname("Ruben")
                .lastname("Stoop")
                .dailyLimit(new BigDecimal(50000))
                .transactionLimit(new BigDecimal(4000));
    }

    @Test
    public void createNewUserFromServiceLayer() {
        DTOEntity userGetDTO = this.userService.addUser(new UserPostDTO().firstname("Test").lastname("User").email("ruben@student.inholland.nl").password("Secret123!"));
    }

    @Test
    public void testLoginUserShouldReturn200() throws Exception {

        String email = "ruben@student.inholland.nl";
        String password = "Secret123!";
        String body = "{\"email\":\"" + email + "\", \"password\":\""
                + password + "\"}";
//        //Initial login request
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()).andReturn();
    }
}