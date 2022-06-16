package io.swagger.controller;

import io.swagger.model.entity.RefreshToken;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserIbanSearchDTO;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserLoginReturnDTO;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.RefreshTokenService;
import io.swagger.service.UserService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebAppConfiguration
@SpringBootTest
class UserControllerV2Test {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    private MockMvc mockMVC;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    private void setup() throws Exception {
        this.mockMVC = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    private String generateJwtToken(String username, UUID id) {
        return jwtTokenProvider.createToken(username, new ArrayList<>(), id);
    }

    @Test
    void addUserShouldReturnStatusCreatedAndObject() throws Exception {
        String body = "{\n" +
                "    \"firstname\": \"Ruben\",\n" +
                "    \"lastname\": \"Stoop\",\n" +
                "    \"email\": \"rubentest@student.inholland.nl\",\n" +
                "    \"password\": \"Secret123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.firstname").value("Ruben"))
                .andExpect(jsonPath("$.lastname").value("Stoop"))
                .andExpect(jsonPath("$.email").value("rubentest@student.inholland.nl"))
                .andExpect(jsonPath("$.transaction_limit").value(50))
                .andExpect(jsonPath("$.daily_limit").value(2500))
                .andExpect(jsonPath("$.role[0].role_id").value(1))
                .andExpect(jsonPath("$.role[0].name").value("ROLE_CUSTOMER"))
                .andExpect(jsonPath("$.role[0].authority").value("ROLE_CUSTOMER"));
    }

    @Test
    void addUserWithTakenEmailShouldReturnStatusConflict() throws Exception {
        String body = "{\n" +
                "    \"firstname\": \"Ruben\",\n" +
                "    \"lastname\": \"Stoop\",\n" +
                "    \"email\": \"tim@student.inholland.nl\",\n" +
                "    \"password\": \"Secret123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(409))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("This email is already in use"));
    }

    @Test
    void addUserWrongInputShouldReturnStatusUnprocessable() throws Exception {
        String body = "{\n" +
                "    \"firsname\": \"Ruben\",\n" +
                "    \"lastname\": \"Stoop\",\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"password\": \"Secret123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode").value(422))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Unprocessable entity."));
    }

    @Test
    void addUserWithBadInputShouldReturnStatusBadRequest() throws Exception {
        String body = "{\n" +
                "    \"firstname\": \"F\",\n" +
                "    \"lastname\": \"L\",\n" +
                "    \"email\": \"E\",\n" +
                "    \"password\": \"P\"\n" +
                "}";
        mockMVC.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Invalid first name length;Invalid last name length;Email address is invalid;Invalid password"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdShouldReturnNoContent() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"15000\",\n" +
                "    \"transaction_limit\": \"15000\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithNoChangesShouldReturnStatusBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithBadUUIDShouldReturnNotFound() throws Exception {
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"15000\",\n" +
                "    \"transaction_limit\": \"15000\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + "123e4567-e89b-12d3-a456-426614174000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithTooHighDailyLimitShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"1000001\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Daily limit should be between 1 and 1.000.000"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithTooHighTransactionLimitShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"transaction_limit\": \"1000001\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Transaction limit should be between 1 and 1.000.000"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithTooLowDailyLimitShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"-1\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Daily limit should be between 1 and 1.000.000"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithTooLowTransactionLimitShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"transaction_limit\": \"-1\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Transaction limit should be between 1 and 1.000.000"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithTakenEmailShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"email\": \"tim@student.inholland.nl\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"15000\",\n" +
                "    \"transaction_limit\": \"15000\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(409))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Email is already in use"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithInvalidEmailShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"email\": \"tim\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"15000\",\n" +
                "    \"transaction_limit\": \"15000\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Email is invalid"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithTooShortFirstNameShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"R\",\n" +
                "    \"lastname\": \"Stoop2\",\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"15000\",\n" +
                "    \"transaction_limit\": \"15000\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Invalid first name length"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void editUserByIdWithTooShortLastNameShouldReturnBadRequest() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String body = "{\n" +
                "    \"firstname\": \"Ruben2\",\n" +
                "    \"lastname\": \"S\",\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"roles\": [1, 2],\n" +
                "    \"daily_limit\": \"15000\",\n" +
                "    \"transaction_limit\": \"15000\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/" + user.getUser_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Invalid last name length"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUserByIdShouldReturnStatusOkAndObject() throws Exception {
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
        String token = this.generateJwtToken(user.getEmail(), user.getUser_id());
        mockMVC.perform(get("/api/users/" + user.getUser_id())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(user.getUser_id().toString()))
                .andExpect(jsonPath("$.firstname").value(user.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(user.getLastname()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.transaction_limit").value(user.getTransactionLimit().intValue()))
                .andExpect(jsonPath("$.daily_limit").value(user.getDailyLimit().intValue()))
                .andExpect(jsonPath("$.role[0].role_id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.role[1].role_id").value(IsNull.notNullValue()));
    }

    @Test
    @WithMockUser(username = "customer@student.inholland.nl", roles = {"CUSTOMER"})
    void getUserByIdWithCustomerRoleShouldReturnStatusOkAndBasicObject() throws Exception {
        User user = userRepo.findByEmail("customer@student.inholland.nl");
        User user2 = userRepo.findByEmail("ruben@student.inholland.nl");
        String token = this.generateJwtToken(user.getEmail(), user.getUser_id());
        mockMVC.perform(get("/api/users/" + user2.getUser_id())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(user2.getUser_id().toString()))
                .andExpect(jsonPath("$.firstname").value(user2.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(user2.getLastname()))
                .andExpect(jsonPath("$.email").value(user2.getEmail()))
                .andExpect(jsonPath("$.transaction_limit").doesNotExist());
    }

    @Test
    @WithMockUser(username = "customer@student.inholland.nl", roles = {"CUSTOMER"})
    void getUserByIdWithUUIDNotFoundShouldReturnStatusOkAndBasicObject() throws Exception {
        User user = userRepo.findByEmail("customer@student.inholland.nl");
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String token = this.generateJwtToken(user.getEmail(), user.getUser_id());
        mockMVC.perform(get("/api/users/" + uuid)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("User with id: '" + uuid + "' not found"));
    }

    @Test
    @WithMockUser(username = "customer@student.inholland.nl", roles = {"CUSTOMER"})
    void getUserByIdWithBadUUIDShouldReturnStatusBadRequest() throws Exception {
        User user = userRepo.findByEmail("customer@student.inholland.nl");
        String uuid = "123e4567-e89b-12d3-a456-426614174000p";
        String token = this.generateJwtToken(user.getEmail(), user.getUser_id());
        mockMVC.perform(get("/api/users/" + uuid)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Invalid UUID string: " + uuid + ""));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersShouldReturnStatusOkAndObject() throws Exception {
        User user1 = userRepo.findByEmail("ruben@student.inholland.nl");
        User user2 = userRepo.findByEmail("tim@student.inholland.nl");
        mockMVC.perform(get("/api/users?pageNo=0&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].user_id").value(user1.getUser_id().toString()))
                .andExpect(jsonPath("$.content.[1].user_id").value(user2.getUser_id().toString()))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersWithBadUrlParametersShouldReturnStatusBadReQuest() throws Exception {
        mockMVC.perform(get("/api/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersWithSpecificParametersShouldReturnStatusOkAndObject() throws Exception {
        UserIbanSearchDTO user = userRepo.findUserByIban("NL01INHO0000000004");
        mockMVC.perform(get("/api/users?pageNo=0&pageSize=3&firstname=Ru&lastname=oop&account=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].user_id").value(user.getUser_id().toString()))
                .andExpect(jsonPath("$.content.[0].iban").value(user.getIban()))
                .andExpect(jsonPath("$.pageable.pageSize").value(3))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersByIbanShouldReturnStatusOkAndObject() throws Exception {
        String iban = "NL01INHO0000000004";
        UserIbanSearchDTO user = userRepo.findUserByIban(iban);
        mockMVC.perform(get("/api/users?pageNo=0&pageSize=3&iban=" + iban))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].user_id").value(user.getUser_id().toString()))
                .andExpect(jsonPath("$.content.[0].iban").value(user.getIban()))
                .andExpect(jsonPath("$.pageable").value("INSTANCE"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersByBadIbanShouldReturnStatusNotFoundAndObject() throws Exception {
        String iban = "NL01INHO0000001004";
        mockMVC.perform(get("/api/users?pageNo=0&pageSize=3&iban=" + iban))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("User with iban: " + iban + " not found."));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersWithTooLowPageNumberShouldReturnStatusBadRequest() throws Exception {
        mockMVC.perform(get("/api/users?pageNo=-1&pageSize=1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Page index must not be less than zero!"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersWithTooLowPageSizeShouldReturnStatusBadRequest() throws Exception {
        mockMVC.perform(get("/api/users?pageNo=0&pageSize=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Page size must not be less than one or more than 20!"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersWithTooHighPageSizeShouldReturnStatusBadRequest() throws Exception {
        mockMVC.perform(get("/api/users?pageNo=0&pageSize=21"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Page size must not be less than one or more than 20!"));
    }

    @Test
    @WithMockUser(username = "ruben@student.inholland.nl", roles = {"CUSTOMER", "EMPLOYEE"})
    void getUsersWithBadPageNumberShouldReturnStatusBadRequest() throws Exception {
        mockMVC.perform(get("/api/users?pageNo=1&pageSize=20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.message").value("Page number not found"));
    }

    @Test
    void loginUserShouldReturnStatusOkAndObject() throws Exception {
        String body = "{\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"password\": \"Secret123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.email").value("ruben@student.inholland.nl"))
                .andExpect(jsonPath("$.access_token").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.refresh_token").value(IsNull.notNullValue()));
    }

    @Test
    void loginUserWithNoEmailShouldReturnStatusBadRequest() throws Exception {
        String body = "{\n" +
                "    \"username\": \"ruben@student.inholland.nl\",\n" +
                "    \"password\": \"Secret123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Email missing"));
    }

    @Test
    void loginUserWithNoPasswordShouldReturnStatusBadRequest() throws Exception {
        String body = "{\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"passwor\": \"Secret123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Password missing"));
    }

    @Test
    void loginUserWithEmailNotFoundShouldReturnStatusNotFound() throws Exception {
        String body = "{\n" +
                "    \"email\": \"test@student.inholland.nl\",\n" +
                "    \"password\": \"Secret123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No account found with given email"));
    }

    @Test
    void loginUserWithBadCredentialsShouldReturnStatusUnauthorized() throws Exception {
        String body = "{\n" +
                "    \"email\": \"ruben@student.inholland.nl\",\n" +
                "    \"password\": \"Badpass123!\"\n" +
                "}";
        mockMVC.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Invalid login credentials"));
    }

    @Test
    void editPasswordShouldReturnNoContent() throws Exception {
        User user = userRepo.findByEmail("customer@student.inholland.nl");
        String token = this.generateJwtToken(user.getEmail(), user.getUser_id());
        String body = "{\n" +
                "    \"currentPassword\": \"Secret123!\",\n" +
                "    \"newPassword\": \"Secret124!\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/password")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    void editPasswordWithBadInputShouldReturnUnprocessable() throws Exception {
        User user = userRepo.findByEmail("customer@student.inholland.nl");
        String token = this.generateJwtToken(user.getEmail(), user.getUser_id());
        String body = "{\n" +
                "    \"currenPassword\": \"Secret123!\",\n" +
                "    \"newPassword\": \"Secret124!\"\n" +
                "}";
        mockMVC.perform(patch("/api/users/password")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void refreshTokenShouldReturnStatusOkAndObject() throws Exception {
        UserLoginReturnDTO userLogin = userService.login(new UserLoginDTO().email("ruben@student.inholland.nl").password("Secret123!"));
        String body = "{\n" +
                "    \"refreshToken\": \"" + userLogin.getRefreshToken() + "\"\n" +
                "}";

        mockMVC.perform(post("/api/users/refreshtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.refreshToken").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void refreshTokeWithBadInputShouldReturnStatusBadRequest() throws Exception {
        UserLoginReturnDTO userLogin = userService.login(new UserLoginDTO().email("ruben@student.inholland.nl").password("Secret123!"));
        String body = "{\n" +
                "    \"refresToken\": \"" + userLogin.getRefreshToken() + "\"\n" +
                "}";

        mockMVC.perform(post("/api/users/refreshtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshTokeWithBadTokenShouldReturnStatusBadRequest() throws Exception {
        String body = "{\n" +
                "    \"refreshToken\": \" badToken \"\n" +
                "}";

        mockMVC.perform(post("/api/users/refreshtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refreshTokeWithTokenNotFoundShouldReturnStatusBadRequest() throws Exception {
        User user = userRepo.findByEmail("customer@student.inholland.nl");
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(user.getUser_id());
        String body = "{\n" +
                "    \"refresToken\": \"" + refreshToken + "\"\n" +
                "}";

        mockMVC.perform(post("/api/users/refreshtoken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}