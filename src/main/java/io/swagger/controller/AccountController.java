package io.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.exception.BadRequestException;
import io.swagger.exception.InternalServerErrorException;
import io.swagger.exception.testException;
import io.swagger.model.account.AccountGetDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.account.AccountPatchDTO;
import io.swagger.model.account.AccountPostDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import io.swagger.model.utils.DTOEntity;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.utils.DtoUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountGetDTO> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Created User object", required=true, schema=@Schema()) @Valid @RequestBody AccountPostDTO body)
    {
        try {
            return new ResponseEntity<AccountGetDTO>(accountService.createAccount(body), HttpStatus.CREATED);
        } catch(Exception exception) {
            throw exception;
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountGetDTO> editAccount(@Parameter(in = ParameterIn.PATH, description = "Iban of account", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "Edit information", required=true, schema=@Schema()) @Valid @RequestBody AccountPatchDTO body) {
        try {
            return new ResponseEntity<AccountGetDTO>(accountService.editAccount(body, iban), HttpStatus.OK);
        } catch(Exception exception) {
            throw exception;
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountGetDTO> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "Iban of account", required=true, schema=@Schema()) @PathVariable("iban") String iban) {
        try {
            return new ResponseEntity<AccountGetDTO>(accountService.getAccount(iban, request), HttpStatus.OK);
        } catch(Exception exception) {
            throw exception;
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AccountGetDTO>> getAccounts(@Parameter(in = ParameterIn.QUERY, description = "This query will get both accounts that belong to the matching user id." ,schema=@Schema()) @Valid @RequestParam(value = "user_id", required = false) String userId, @Parameter(in = ParameterIn.QUERY, description = "This query will filter either the 'primary' or 'savings' account." ,schema=@Schema(allowableValues={ "primary", "savings" }
    )) @Valid @RequestParam(value = "type", required = false) List<String> type) {
        try {
            return new ResponseEntity<List<AccountGetDTO>>(accountService.getAccounts(userId, type, request), HttpStatus.OK);
        } catch(Exception exception) {
            throw exception;
        }
    }
}
