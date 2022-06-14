package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository repo;

    @Test
    void findByFromAccountOrToAccount() {
        String iban = "NL01INHO0000000004";
        Pageable pageable = PageRequest.of(0, 5);
        boolean checkThatListContainsNonMatchingTransactions = false;

        List<Transaction> allTransactions = repo.findAll();
        for(Transaction t : allTransactions){
            if(t.getFromAccount().equals(iban) || t.getToAccount().equals(iban)){
                checkThatListContainsNonMatchingTransactions = true;
            }
        }
        Assertions.assertThat(checkThatListContainsNonMatchingTransactions).isTrue();

        boolean checkThatListContainsOnlyMatchingTransactions = false;
        List<Transaction> transactions = repo.findByFromAccountOrToAccount(iban, iban, pageable);

        for(Transaction t : transactions){
            if(!t.getFromAccount().equals(iban) && !t.getToAccount().equals(iban)){
                checkThatListContainsOnlyMatchingTransactions = true;
            }
        }
        Assertions.assertThat(checkThatListContainsOnlyMatchingTransactions == false).isTrue();
    }
}