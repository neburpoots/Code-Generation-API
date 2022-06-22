package io.swagger.cucumber.glue.transactionSteps;

import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

public abstract class TransactionBaseSteps {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected final String baseUrl = "http://localhost:";

    @LocalServerPort
    protected int serverPort;
}
