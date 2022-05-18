/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.34).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.controller;

import io.swagger.model.utils.DTOEntity;
import io.swagger.model.utils.Error;
import io.swagger.model.entity.User;
import io.swagger.model.user.UserLoginDTO;
import io.swagger.model.user.UserPasswordDTO;
import io.swagger.model.user.UserPatchDTO;
import io.swagger.model.user.UserPostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")
@Validated
public interface UserControllerInterface {

    @Operation(summary = "User registration", description = "Returns a list of Users, filtered by parameters and pagination.", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "404", description = "Resource not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "409", description = "There was a conflict processing your request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))})
    @RequestMapping(value = "/api/users",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<DTOEntity> addUser(@Parameter(in = ParameterIn.DEFAULT, description = "Created User object", required = true, schema = @Schema()) @Valid @RequestBody UserPostDTO body);


    @Operation(summary = "Changes password of the logged in User", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully changed the password."),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "404", description = "Resource not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))})
    @RequestMapping(value = "/api/users/password",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    ResponseEntity<Void> editPassword(@Parameter(in = ParameterIn.DEFAULT, description = "Password information", required = true, schema = @Schema()) @Valid @RequestBody UserPasswordDTO body);


    @Operation(summary = "Edit a User by id", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully saved changes."),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "404", description = "Resource not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))})
    @RequestMapping(value = "/api/users/{id}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    ResponseEntity<Void> editUserById(@Parameter(in = ParameterIn.PATH, description = "Id of the user you want to edit", required = true, schema = @Schema()) @PathVariable("id") String id, @Parameter(in = ParameterIn.DEFAULT, description = "Created User object", required = true, schema = @Schema()) @Valid @RequestBody UserPatchDTO body);


    @Operation(summary = "Find a User by id", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "404", description = "Resource not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))})
    @RequestMapping(value = "/api/users/{id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<DTOEntity> getUserById(@Parameter(in = ParameterIn.PATH, description = "Id of the user you want to get", required = true, schema = @Schema()) @PathVariable("id") String id);


    @Operation(summary = "Finds Users by name and/or iban", description = "Returns a list of Users, filtered by parameters and pagination.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Object.class)))),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "404", description = "Resource not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))})
    @RequestMapping(value = "/api/users",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<DTOEntity>> getUsers(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Page number for pagination", required = true, schema = @Schema()) @Valid @RequestParam(value = "page", required = true) Integer page, @Parameter(in = ParameterIn.QUERY, description = "First name value that needs to be considered for filter", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "Last name value that needs to be considered for filter", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "IBAN value that needs to be considered for filter", schema = @Schema()) @Valid @RequestParam(value = "iban", required = false) String iban);


    @Operation(summary = "Logs User into the system", description = "", tags = {"users"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "404", description = "Resource not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))),

            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)))})
    @RequestMapping(value = "/api/users/login",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<DTOEntity> loginUser(@Parameter(in = ParameterIn.DEFAULT, description = "Login credentials", required = true, schema = @Schema()) @Valid @RequestBody UserLoginDTO body);

}

