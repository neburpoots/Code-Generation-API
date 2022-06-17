package io.swagger.cucumber.glue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.UserService;
import io.swagger.utils.RestPageImpl;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserSteps {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User expectedUser;

    private User actualUser;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private HttpHeaders headers;

    public void setup()
    {
        expectedUser = new User();
        actualUser = new User();
        headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

//        userService.addUser(new UserPostDTO().firstname("Ruben").lastname("Stoop").email("ruben@student.inholland.nl").password("Secret123!"));


        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "EMPLOYEE"));
        roles.add(new Role(2, "CUSTOMER"));


        headers.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", roles, UUID.randomUUID()));
    }

    @Given("^the following users$")
    public void givenTheFollowingUsers(final User user)
    {
        expectedUser = user;
    }


    @When("^the user requests all the users")
    public void whenTheUserRequestAllTheUsers() throws IOException {

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<RestPageImpl<User>> users =

                        testRestTemplate.exchange("/api/users?pageNo=0&pageSize=1", HttpMethod.GET, request, new ParameterizedTypeReference<RestPageImpl<User>>() {
                        });

        actualUser = users.getBody().getContent().get(0);
    }

    @Then("^all the users are returned")
    public void thenAllTheUsersAreReturned() {
        validateUsers();
    }

    private void validateUsers() {
        validateUser(expectedUser, actualUser);
    }

    private void validateUser(final User expected, final User actual) {
        Assertions.assertEquals(expected.getFirstname(), actual.getFirstname());
        Assertions.assertEquals(expected.getLastname(), actual.getLastname());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
    }
}
