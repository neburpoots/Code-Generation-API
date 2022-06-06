package io.swagger.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;


import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
class TransactionServiceTest {

    @MockBean
    private TransactionService transactionsService;

    @Test
    void getTransactionById() {
        String id = "NL01INHO0000000004";
    }

    @Test
    void getTransactionObjectById() {


    }

    @Test
    void validateBigDecimal() {

        boolean validated = this.transactionsService.validateBigDecimal("20");
    }

    @Test
    void validateIban() throws Exception {



        assertTrue(true);

    }

    @Test
    void validateTransactionDTO() {
    }

    @Test
    void validateSome() {
    }

    @Test
    void createTransaction() {
    }

    @Test
    void convertListToGetDto() {
    }

    @Test
    void addTransactionType() {
    }

    @Test
    void filterTransactions() {
    }
}