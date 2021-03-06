package io.swagger.controller;

import io.swagger.annotations.Api;
import io.swagger.model.entity.Transaction;
import io.swagger.model.transaction.FilterParams;
import io.swagger.model.transaction.TransactionPostDTO;
import io.swagger.model.utils.DTOEntity;
import io.swagger.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.Generated;
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

    public ResponseEntity<DTOEntity> addTransaction(TransactionPostDTO body) {
        try {
            return new ResponseEntity<DTOEntity>(this.transactionService.createTransaction(body, request), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    public ResponseEntity<DTOEntity> getTransactionById(String id) {
        try {
            return new ResponseEntity<DTOEntity>(this.transactionService.getTransactionById(id, this.request), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    public ResponseEntity <Page<Transaction>> getTransactions(Integer page, Integer pageSize, FilterParams filterParams){
        try {
            return new ResponseEntity<Page<Transaction>>(this.transactionService.filterTransactions(page, pageSize, filterParams, this.request), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }
}
