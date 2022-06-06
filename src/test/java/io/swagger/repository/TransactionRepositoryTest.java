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
    void filterTransactions() {
        LocalDate now = LocalDate.now();
        String date = now.getDayOfMonth() + "-" + now.getMonthValue() + "-" + now.getYear();
        //search params
        ArrayList p = new ArrayList();
        p.add("?from_iban=NL01INHO0000000004");
        p.add("&to_iban=NL01INHO0000000005");

        Pageable pageable = PageRequest.of(0, 10);
        List<Transaction> existingTransactions = repo.findAll();
        Assertions.assertThat(existingTransactions.size() > 8);

        List<Transaction> transactions = repo.filterTransactions(p.get(0).toString(),
                p.get(1).toString(),
                LocalDate.now(),
                 new BigDecimal(30),
                new BigDecimal(1000),
                new BigDecimal(10), pageable);

        Assertions.assertThat(transactions.size() < 5);
    }

    @Test
    void findByFromAccountAndTimestampAfterAndTypeOrType() {

    }

    @Test
    void findByFromAccountOrToAccount() {
    }
}