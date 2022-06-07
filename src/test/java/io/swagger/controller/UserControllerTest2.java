package io.swagger.controller;

import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.apache.tomcat.jni.Local;
import org.hamcrest.core.IsNull;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebAppConfiguration
@SpringBootTest
public class UserControllerTest2 {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepo;

    private MockMvc mockMVC;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    private void setup() throws Exception {
        this.mockMVC = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    private String generateJwtToken(String username, UUID id) {
        return jwtTokenProvider.createToken(username, new ArrayList<Role>(), id);
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
        User user = userRepo.findByEmail("ruben@student.inholland.nl");
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
        String body = "{\n" +
                "\n" +
                "}";
//        List<Role> roles = new ArrayList<Role>();
//        roles.addAll(user.getRoles());
//        Collections.sort(roles, new Comparator<Role>() {
//            @Override
//            public int compare(Role r1, Role r2) {
//                return r1.getRole_id().compareTo(r2.getRole_id());
//            }
//        });
        mockMVC.perform(get("/api/users/" + user.getUser_id())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(user.getUser_id().toString()))
                .andExpect(jsonPath("$.firstname").value(user.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(user.getLastname()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.transaction_limit").value(user.getTransactionLimit().intValue()))
                .andExpect(jsonPath("$.daily_limit").value(user.getDailyLimit().intValue()));
//                .andExpect(jsonPath("$.role[0].role_id").value(roles.get(0).getRole_id()))
//                .andExpect(jsonPath("$.role[1].role_id").value(roles.get(1).getRole_id()));
    }

    @Test
    @WithMockUser(username = "customer@student.inholland.nl", roles = {"CUSTOMER"})
    void getUserByIdWithCustomerRoleShouldReturnStatusOkAndBasicObject() throws Exception {
        User user = userRepo.findByEmail("customer@student.inholland.nl");
        User user2 = userRepo.findByEmail("ruben@student.inholland.nl");
        String token = this.generateJwtToken(user.getEmail(), user.getUser_id());
        List<Role> roles = new ArrayList<Role>();
        roles.addAll(user.getRoles());
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
    void getUsers() {
    }

    @Test
    void loginUser() {
    }

    //    @Test
//    void editPassword() throws Exception{
//        String body = "{\n" +
//                "    \"currentPassword\": \"Secret123!\",\n" +
//                "    \"newPassword\": \"Secret124!\"\n" +
//                "}";
//        mockMVC.perform(patch("/api/users/password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.statusCode").value(400))
//                .andExpect(jsonPath("$.timestamp").value(IsNull.notNullValue()))
//                .andExpect(jsonPath("$.message").value("Invalid first name length;Invalid last name length;Email address is invalid;Invalid password"));
//    }

    @Test
    void refreshToken() {
    }
}