package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.model.user.*;
import io.swagger.model.utils.DTOEntity;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.RefreshTokenService;
import io.swagger.service.UserService;
import io.swagger.utils.DtoUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserService.class, UserController.class})
@WebMvcTest(UserController.class)
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

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.userService = new UserService(userRepo, roleRepo, authenticationManager, jwtTokenProvider, passwordEncoder, dtoUtils, refreshTokenService);
    }

    @Test
    void addUserShouldReturnStatusCreatedAndObject() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";
        UserPostDTO dtoP = new UserPostDTO()
                .firstname("Ruben")
                .lastname("Stoop")
                .password("Secret123!")
                .email("ruben@student.inholland.nl");
        UserGetDTO userG = new UserGetDTO()
                .user_id(UUID.fromString(uuid))
                .email("ruben@student.inholland.nl")
                .firstname("Ruben")
                .lastname("Stoop")
                .dailyLimit(new BigDecimal(5000))
                .transactionLimit(new BigDecimal(4000));

        when(userController.addUser(dtoP))
                .thenReturn(new ResponseEntity<UserGetDTO>(userG, HttpStatus.CREATED)
                );
        this.mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoP)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id").value(uuid))
                .andExpect(jsonPath("$.firstname").value("Ruben"))
                .andExpect(jsonPath("$.lastname").value("Stoop"))
                .andExpect(jsonPath("$.email").value("ruben@student.inholland.nl"))
                .andExpect(jsonPath("$.daily_limit").value(5000))
                .andExpect(jsonPath("$.transaction_limit").value(4000));
    }

    @Test
    void editPasswordShouldReturnStatusNoContent() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";
        UserPasswordDTO dtoP = new UserPasswordDTO()
                .currentPassword("Secret123!")
                .newPassword("Secret1234!");

        when(userController.editPassword(dtoP))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
                );
        this.mockMvc.perform(patch("/api/users/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoP)))
                .andExpect(status().isNoContent());
    }

    @Test
    void editUserByIdShouldReturnStatusNoContent() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";
        UserPatchDTO dtoP = new UserPatchDTO()
                .firstname("Ruben")
                .lastname("Stoop")
                .email("ruben@student.inholland.nl")
                .dailyLimit(new BigDecimal(500));

        when(userController.editUserById(uuid, dtoP))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)
                );
        this.mockMvc.perform(patch("/api/users/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoP)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserByIdShouldReturnStatusOKAndOneObject() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";

        when(userController.getUserById(uuid))
                .thenReturn(new ResponseEntity<DTOEntity>(new UserSearchDTO()
                        .user_id(UUID.fromString(uuid))
                        .firstname("Ruben")
                        .lastname("Stoop")
                        .email("ruben@student.inholland.nl"), HttpStatus.OK)
                );
        mockMvc.perform(get("/api/users/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value("f963d334-2a06-4e75-96d9-16cbb8b0c2b3"))
                .andExpect(jsonPath("$.firstname").value("Ruben"))
                .andExpect(jsonPath("$.lastname").value("Stoop"))
                .andExpect(jsonPath("$.email").value("ruben@student.inholland.nl"));
    }

    @Test
    void getUsersShouldReturnListOfObjectsAndStatusOk() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";
        UserIbanSearchDTO userG = new UserIbanSearchDTO()
                .user_id(UUID.fromString(uuid))
                .firstname("Ruben")
                .lastname("Stoop")
                .email("ruben@student.inholland.nl")
                .iban("NL01INHO0000000004");
        Pageable pageable = PageRequest.of(0, 5);
        Page page = new PageImpl(List.of(userG), pageable, 15);


        when(userController.getUsers(0, 5, null, null, null, null))
                .thenReturn(new ResponseEntity<Page<DTOEntity>>(page, HttpStatus.OK));
        mockMvc.perform(get("/api/users?pageNo=0&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].user_id").value("f963d334-2a06-4e75-96d9-16cbb8b0c2b3"))
                .andExpect(jsonPath("$.content[0].firstname").value("Ruben"))
                .andExpect(jsonPath("$.content[0].lastname").value("Stoop"))
                .andExpect(jsonPath("$.content[0].email").value("ruben@student.inholland.nl"))
                .andExpect(jsonPath("$.content[0].iban").value("NL01INHO0000000004"))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(5))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.totalElements").value(15));

        mockMvc.perform(get("/api/users?pageNo=0&pageSize=5"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void loginUserShouldReturnStatusOkAndObject() throws Exception {
        String uuid = "f963d334-2a06-4e75-96d9-16cbb8b0c2b3";
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJydWJlbkBzdHVkZW50LmluaG9sbGFuZC5ubCIsImF1dGgiOltdLCJhdWQiOiIwYTMwNGUyYS1iMTBjLTQ0MDYtOGE4ZS0xZmU1ZTMyMzBmZjkiLCJpYXQiOjE2NTQ1NDg5NzQsImV4cCI6MTY1NDU4NDk3NH0.I1-s6F3j4iHiiDLnUYFtOANES83vAmk96JzOY7f9fN8";
        String refreshToken = "aee7701a-9c65-4916-9c8e-be37909e1f37";

        UserLoginDTO dtoP = new UserLoginDTO()
                .email("ruben@student.inholland.nl")
                .password("Secret123!");
        UserLoginReturnDTO userG = new UserLoginReturnDTO()
                .user_id(UUID.fromString(uuid))
                .email("ruben@student.inholland.nl")
                .firstname("Ruben")
                .lastname("Stoop")
                .dailyLimit(new BigDecimal(5000))
                .transactionLimit(new BigDecimal(4000))
                .role(List.of(new Role(1, "ROLE_CUSTOMER"), new Role(2, "ROLE_EMPLOYEE")))
                .accessToken(accessToken)
                .refreshToken(refreshToken);

        when(userController.loginUser(dtoP))
                .thenReturn(new ResponseEntity<UserLoginReturnDTO>(userG, HttpStatus.OK)
                );
        this.mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoP)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(uuid))
                .andExpect(jsonPath("$.firstname").value("Ruben"))
                .andExpect(jsonPath("$.lastname").value("Stoop"))
                .andExpect(jsonPath("$.email").value("ruben@student.inholland.nl"))
                .andExpect(jsonPath("$.daily_limit").value(5000))
                .andExpect(jsonPath("$.transaction_limit").value(4000))
                .andExpect(jsonPath("$.role[0].role_id").value(1))
                .andExpect(jsonPath("$.role[0].name").value("ROLE_CUSTOMER"))
                .andExpect(jsonPath("$.role[0].authority").value("ROLE_CUSTOMER"))
                .andExpect(jsonPath("$.role[1].role_id").value(2))
                .andExpect(jsonPath("$.role[1].name").value("ROLE_EMPLOYEE"))
                .andExpect(jsonPath("$.role[1].authority").value("ROLE_EMPLOYEE"))
                .andExpect(jsonPath("$.access_token").value(accessToken))
                .andExpect(jsonPath("$.refresh_token").value(refreshToken));
    }

    @Test
    void refreshTokenShouldReturnStatusOkAndObject() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJydWJlbkBzdHVkZW50LmluaG9sbGFuZC5ubCIsImF1dGgiOltdLCJhdWQiOiIwYTMwNGUyYS1iMTBjLTQ0MDYtOGE4ZS0xZmU1ZTMyMzBmZjkiLCJpYXQiOjE2NTQ1NDg5NzQsImV4cCI6MTY1NDU4NDk3NH0.I1-s6F3j4iHiiDLnUYFtOANES83vAmk96JzOY7f9fN8";
        String refreshToken = "aee7701a-9c65-4916-9c8e-be37909e1f37";

        TokenRefreshRequestDTO dtoP = new TokenRefreshRequestDTO();
        dtoP.setRefreshToken(refreshToken);
        TokenRefreshResponseDTO dtoG = new TokenRefreshResponseDTO();
        dtoG.setAccessToken(accessToken);
        dtoG.setRefreshToken(refreshToken);

        when(userController.refreshToken(dtoP))
                .thenReturn(new ResponseEntity<TokenRefreshResponseDTO>(dtoG, HttpStatus.OK));
        this.mockMvc.perform(post("/api/users/refreshtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoP)))
                .andExpect(status().isOk());
    }

    @Test
    void addUserShouldReturnStatusCreated() throws Exception {
        String email = "ruben@student.inholland.nl";
        String password = "Secret123!";
        String body = "{\"email\":\"" + email + "\", \"password\":\""
                + password + "\"}";

        //Initial login request
        this.mockMvc.perform(post("http://localhost:8080/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void testLoginUserShouldReturnStatusOK() throws Exception {
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
    @WithMockUser(roles = "EMPLOYEE")
    void getUserWithRoleEmployeeShouldReturnStatusOKAndOneObject() throws Exception {
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

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getUsersWithBadParametersShouldReturnStatusBadRequest() throws Exception {
        mockMvc.perform(get("/api/users?&pageSize=5"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getUsersWithRoleEmployeeShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/api/users?pageNo=0&pageSize=5"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void EditUserByIdWithRoleEmployeeShouldReturnStatusOk() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("firstname", "Ruben");
        payload.put("lastname", "Stoop");
        payload.put("email", "ruben@student.inholland.nl");
        payload.put("dailyLimit", 1001);
        payload.put("transactionLimit", 1002);

        mockMvc.perform(patch("/api/users/f963d334-2a06-4e75-96d9-16cbb8b0c2b3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload.toString()))
                .andExpect(status().isOk())
                .andReturn();
    }
}