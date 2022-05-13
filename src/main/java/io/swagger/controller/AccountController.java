package io.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.model.entity.Account;
import io.swagger.model.AccountPatchDTO;
import io.swagger.model.AccountPostDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")
@RestController
@Api(tags = {"accounts"})
public class AccountController implements AccountControllerInterface {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final AccountService accountService;

    @Autowired
    private UserService userService;


    @org.springframework.beans.factory.annotation.Autowired
    public AccountController(ObjectMapper objectMapper, HttpServletRequest request, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.accountService = accountService;
    }

    public ResponseEntity<Account> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Created User object", required=true, schema=@Schema()) @Valid @RequestBody AccountPostDTO body) {
        Account account2 = new Account(new BigDecimal(500), new BigDecimal(500), AccountType.SAVINGS, true);

        List<User> Allusers = userService.getUsers();
        account2.setUser(Allusers.get(1));
        return new ResponseEntity<Account>(accountService.createAccount(account2), HttpStatus.OK);
    }

    public ResponseEntity<Void> editAccount(@Parameter(in = ParameterIn.PATH, description = "Iban of account", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "Edit information", required=true, schema=@Schema()) @Valid @RequestBody AccountPatchDTO body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Account> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "Iban of account", required=true, schema=@Schema()) @PathVariable("iban") String iban) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Account>(objectMapper.readValue("{\n  \"balance\" : 2042.67,\n  \"user_id\" : \"123e4567-e89b-12d3-a456-426614174000\",\n  \"iban\" : \"NL41INHO0546284337\",\n  \"absolute_limit\" : -1000,\n  \"type\" : {\n    \"name\" : \"Primary\",\n    \"id\" : 0\n  },\n  \"status\" : true\n}", Account.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Account>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Account>> getAccounts(@Parameter(in = ParameterIn.QUERY, description = "This query will get both accounts that belong to the matching user id." ,schema=@Schema()) @Valid @RequestParam(value = "user_id", required = false) String userId,@Parameter(in = ParameterIn.QUERY, description = "This query will filter either the 'primary' or 'savings' account." ,schema=@Schema(allowableValues={ "primary", "savings" }
    )) @Valid @RequestParam(value = "type", required = false) List<String> type) {

        return new ResponseEntity<List<Account>>(accountService.getAccounts(), HttpStatus.OK);
    }
}
