/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.34).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.controller;

import io.swagger.model.entity.Transaction;
import io.swagger.model.transaction.FilterDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")
@Validated
public interface TransactionControllerInterface {

    @Operation(summary = "Make a Transaction", description = "Makes a transaction.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"transactions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served."),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing."),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request."),

            @ApiResponse(responseCode = "404", description = "Resource not found."),

            @ApiResponse(responseCode = "409", description = "There was a conflict processing your request."),

            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @RequestMapping(value = "/api/transactions",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<DTOEntity> addTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Created Transaction object", required = true, schema = @Schema()) @Valid @RequestBody TransactionPostDTO body);


    @Operation(summary = "Finds a Transaction based on id", description = "Returns transaction information matching the provided id.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"transactions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served."),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing."),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request."),

            @ApiResponse(responseCode = "404", description = "Resource not found."),

            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @RequestMapping(value = "/api/transactions/{id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<DTOEntity> getTransactionById(
            @Parameter(in = ParameterIn.PATH, description = "All transaction of given iban (id)", required = true, schema = @Schema()) @PathVariable("id") String id);


    @Operation(summary = "Finds Transactions by date, user, iban or by amount", description = "Returns a list of Transactions, filtered by parameters and pagination.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"transactions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Object.class)))),

            @ApiResponse(responseCode = "400", description = "The request was invalid or cannot be served."),

            @ApiResponse(responseCode = "401", description = "Credentials invalid or missing."),

            @ApiResponse(responseCode = "403", description = "You are not authorized to make this request."),

            @ApiResponse(responseCode = "404", description = "Resource not found."),

            @ApiResponse(responseCode = "500", description = "Internal server error.")})
    @RequestMapping(value = "/api/transactions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<DTOEntity>> getTransactions(@Parameter(in = ParameterIn.QUERY, description = "Page number for pagination", required = true, schema = @Schema()) @Valid @RequestParam(value = "page", required = true, defaultValue = "0") Integer page,
    @Parameter(in = ParameterIn.QUERY, description = "Page size for pagination", required = true, schema = @Schema()) @Valid @RequestParam(value = "page_size", required = true, defaultValue = "10") Integer pageSize, @Valid FilterDTO filterDTO);
}

/**
 * @Parameter(in = ParameterIn.QUERY, description = "Page number for pagination", required = true, schema = @Schema()) @Valid @RequestParam(value = "page", required = true, defaultValue = "0") Integer page,
 *             @Parameter(in = ParameterIn.QUERY, description = "Page size for pagination", required = true, schema = @Schema()) @Valid @RequestParam(value = "page_size", required = true, defaultValue = "10") Integer pageSize,
 *             @Parameter(in = ParameterIn.QUERY, description = "The from date, filtering transaction after this date.  ", schema = @Schema()) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "from_date", required = false) Date fromDate,
 *             @Parameter(in = ParameterIn.QUERY, description = "The untill date, filtering transactions before this date. ", schema = @Schema()) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value = "until_date", required = false) Date untilDate,
 *             @Parameter(in = ParameterIn.QUERY, description = "From IBAN account that needs to be considered for filter", schema = @Schema()) @Pattern(regexp = "[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{6,14}", message = "Iban of the from account was in invalid form.") @RequestParam(value = "from_iban", required = false) String fromIban,
 *             @Parameter(in = ParameterIn.QUERY, description = "To IBAN account that needs to be considered for filter", schema = @Schema()) @Pattern(regexp = "[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{6,14}", message = "Iban of the to account was in invalid form.") @RequestParam(value = "to_iban", required = false) String toIban,
 *             @Parameter(in = ParameterIn.QUERY, description = "Equals given amount that needs to be considered for filter", schema = @Schema()) @Min(value = 0, message = "Equals amount must be number and can not be lower than zero. ") @RequestParam(value = "amount_equals", required = false) String amountEquals,
 *             @Parameter(in = ParameterIn.QUERY, description = "Less than given amount that needs to be considered for filter", schema = @Schema()) @Min(value = 0, message = "Lower than amount must be a nummber and can not be lower than zero. ") @RequestParam(value = "amount_lower_than", required = false) String amountLowerThan,
 *             @Parameter(in = ParameterIn.QUERY, description = "More than given amount that needs to be considered for filter", schema = @Schema()) @Min(value = 0, message = "More than amount must be an number and can not be lower than zero. ") @RequestParam(value = "amount_more_than", required = false) String amountMoreThan,
 *             @Parameter(in = ParameterIn.QUERY, description = "Filter options for transactions. ", required = false, schema = @Schema()) @Valid @RequestBody FilterDTO filterDTO
 */