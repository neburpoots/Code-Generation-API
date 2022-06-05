package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserLoginReturnDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.RefreshTokenService;
import io.swagger.service.UserService;
import io.swagger.utils.DtoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.userService = new UserService(userRepo, roleRepo, authenticationManager, jwtTokenProvider, passwordEncoder, dtoUtils, refreshTokenService);
//        this.userController = new UserController(new ObjectMapper(), request, this.userService);

    }

    @Test
    public void registerUserShouldReturn201Created() throws Exception {


//
        String username = "ruben@student.inholland.nl";
        String password = "Secret123!";
//
        String body = "{\"username\":\"" + username + "\", \"password\":\""
                + password + "\"}";
//        //Initial login request
        this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()).andReturn();

    }





    @Test
    public void testLoginUserShouldReturn200() throws Exception {

        when(userController.loginUser(
                new UserLoginDTO()
                        .password("Secret123!")
                        .email("ruben@student.inholland.nl")))
                .thenReturn(new ResponseEntity<DTOEntity>(new UserLoginReturnDTO()
                                .user_id(UUID.randomUUID())
                                .accessToken("flajsdfoiaewfase.fasjeofjasefjoas.afejoasfjaesi")
                                .refreshToken("fjaoesfjasef.aefjasiofjioasf.asjifoasjefoie")
                                .email("ruben@student.inholland.nl"), HttpStatus.OK)
                        );


//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addParameter("email", "ruben@student.inholland.nl");
//        request.addParameter("password", "Secret123!");

//
        String username = "ruben@student.inholland.nl";
        String password = "Secret123!";
//
        String body = "{\"username\":\"" + username + "\", \"password\":\""
                + password + "\"}";
//        //Initial login request
        this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()).andReturn();
//        JSONAssert.assertEquals(expected, data, false);
//
//        String resultCZ = result.getResponse().getContentAsString();
//        assertNotNull(resultCZ);

//        String response = result.getResponse().getContentAsString();
//        response = response.replace("{\"access_token\": \"", "");
//        String token = response.replace("\"}", "");
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/test")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk());
    }
}