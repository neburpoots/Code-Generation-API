package io.swagger.cucumber.glue.userSteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.exception.ErrorMessage;
import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public abstract class UserBaseSteps
{
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected WebSecurityConfig webSecurityConfig;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected final String baseUrl = "http://localhost:";
    @LocalServerPort
    protected int serverPort;

    protected User exampleUser;
    protected ResponseEntity<String> response;
    protected ErrorMessage output;

    protected void makeExampleUser() {
        this.exampleUser = new User("Example", "User", "emailUser@example.com", new BigDecimal(50), new BigDecimal(1500), webSecurityConfig.passwordEncoder().encode("Secret123!"));
        this.exampleUser = this.userRepository.save(exampleUser);
    }

    protected HttpHeaders getAuthorizedHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type",
                "application/json");
        httpHeaders.add("Authorization", "Bearer " + jwtTokenProvider.createToken("ruben@student.inholland.nl", new ArrayList<>(), UUID.randomUUID()));
        return httpHeaders;
    }
}
