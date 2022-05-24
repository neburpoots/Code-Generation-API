package io.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.exception.InternalServerErrorException;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserPasswordDTO;
import io.swagger.model.user.UserPatchDTO;
import io.swagger.model.user.UserPostDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.utils.DTOEntity;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Generated;
import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")
@RestController
@Api(tags = {"users"})
public class UserController implements UserControllerInterface {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final UserService userService;

    @Autowired
    public UserController(ObjectMapper objectMapper, HttpServletRequest request, UserService userService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userService = userService;
    }

    public ResponseEntity<DTOEntity> addUser(@Parameter(in = ParameterIn.DEFAULT, description = "Created User object", required = true, schema = @Schema()) @Valid @RequestBody UserPostDTO body) {
        try {
            return new ResponseEntity<DTOEntity>(this.userService.addUser(body), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    public ResponseEntity<Void> editPassword(@Parameter(in = ParameterIn.DEFAULT, description = "Password information", required = true, schema = @Schema()) @Valid @RequestBody UserPasswordDTO body) {
        try {
            if (this.userService.editPassword(body, request)) {
                return new ResponseEntity<Void>(HttpStatus.OK);
            } else {
                throw new InternalServerErrorException();
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> editUserById(@Parameter(in = ParameterIn.PATH, description = "Id of the user you want to edit", required = true, schema = @Schema()) @PathVariable("id") String id, @Parameter(in = ParameterIn.DEFAULT, description = "Created User object", required = true, schema = @Schema()) @Valid @RequestBody UserPatchDTO body) {
        try {
            this.userService.editUserById(body, id);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    public ResponseEntity<DTOEntity> getUserById(@Parameter(in = ParameterIn.PATH, description = "Id of the user you want to get", required = true, schema = @Schema()) @PathVariable("id") String id) {
        try {
            return new ResponseEntity<DTOEntity>(this.userService.getUserById(id, request), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<DTOEntity>> getUsers(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Page number for pagination", required = true, schema = @Schema()) @Valid @RequestParam(value = "page", required = true) Integer page, @Parameter(in = ParameterIn.QUERY, description = "First name value that needs to be considered for filter", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "Last name value that needs to be considered for filter", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "IBAN value that needs to be considered for filter", schema = @Schema()) @Valid @RequestParam(value = "iban", required = false) String iban) {
        try {
            return new ResponseEntity<List<DTOEntity>>(this.userService.getUsers(firstname, lastname, iban), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    public ResponseEntity<DTOEntity> loginUser(@Parameter(in = ParameterIn.DEFAULT, description = "Login credentials", required = true, schema = @Schema()) @Valid @RequestBody UserLoginDTO body) {
        try {
            return new ResponseEntity<DTOEntity>(this.userService.login(body), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

}
