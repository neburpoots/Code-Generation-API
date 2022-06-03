package io.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.model.transaction.TransactionGetDTO;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.service.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.processing.Generated;
import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")
@RestController
@Api(tags = {"transactions"})
public class TransactionController implements TransactionControllerInterface {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final HttpServletRequest request;

    private TransactionService transactionService;

    @Autowired
    public TransactionController(HttpServletRequest request, TransactionService transactionService) {
        this.request = request;
        this.transactionService = transactionService;
    }

    public ResponseEntity<DTOEntity> addTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Created Transaction object", required = true,
            schema = @Schema()) @Valid @RequestBody TransactionPostDTO body) {
        try {
            return new ResponseEntity<DTOEntity>(this.transactionService.createTransaction(body), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    public ResponseEntity<DTOEntity> getTransactionById(@Parameter(in = ParameterIn.PATH, description = "Id of transaction", required = true, schema = @Schema()) @PathVariable("id") String id) {
        try {
            return new ResponseEntity<DTOEntity>(this.transactionService.getTransactionById(id), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    public ResponseEntity<List<TransactionGetDTO>> getTransactions(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Page number for pagination",
            required = true, schema = @Schema()) @Valid @RequestParam(value = "page",
            required = true, defaultValue = "0") Integer page, @NotNull @Parameter(in = ParameterIn.QUERY, description = "Page size for pagination",
            required = true, schema = @Schema()) @Valid @RequestParam(value = "pageSize",
            required = true, defaultValue = "10") Integer pageSize ,@Parameter(in = ParameterIn.QUERY, description = "Date value that needs to be considered for filter",
            schema = @Schema()) @Valid @RequestParam(value = "date", required = false, defaultValue = "") String transactionDate, @Parameter(in = ParameterIn.QUERY, description = "From IBAN account that needs to be considered for filter",
            schema = @Schema()) @Valid @RequestParam(value = "from_iban", required = false, defaultValue = "") String fromIban, @Parameter(in = ParameterIn.QUERY, description = "To IBAN account that needs to be considered for filter",
            schema = @Schema()) @Valid @RequestParam(value = "to_iban", required = false, defaultValue = "") String toIban, @Parameter(in = ParameterIn.QUERY, description = "Equals given amount that needs to be considered for filter",
            schema = @Schema()) @Valid @RequestParam(value = "as_eq", required = false, defaultValue = "") String asEq, @Parameter(in = ParameterIn.QUERY, description = "Less than given amount that needs to be considered for filter",
            schema = @Schema()) @Valid @RequestParam(value = "as_lt", required = false, defaultValue = "") String asLt, @Parameter(in = ParameterIn.QUERY, description = "More than given amount that needs to be considered for filter",
            schema = @Schema()) @Valid @RequestParam(value = "as_mt", required = false, defaultValue = "") String asMt) {
        try {
            //return new ResponseEntity<List<TransactionGetDTO>>(this.transactionService.getTransactions(fromIban, toIban, asEq, asLt, asMt, transactionDate), HttpStatus.OK);
            return new ResponseEntity<List<TransactionGetDTO>>(this.transactionService.filterTransactions(fromIban, toIban, asEq, asLt, asMt, transactionDate, page, pageSize), HttpStatus.OK);

        } catch (Exception exception) {
            throw exception;
        }
    }
}
