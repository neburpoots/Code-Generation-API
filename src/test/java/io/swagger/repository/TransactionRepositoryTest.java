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


        for(Transaction t : transactions){
            Assertions.assertThat(t.getAmount().compareTo(new BigDecimal(10)) == 1);
            Assertions.assertThat(t.getFromAccount() == p.get(0).toString());
            Assertions.assertThat(t.getToAccount() == p.get(1).toString());
            Assertions.assertThat(t.getAmount().compareTo(new BigDecimal(30)) == 0);
            Assertions.assertThat(t.getAmount().compareTo(new BigDecimal(1000)) == -1);
        }

        //Assert that filtering has removed transactions from results.
        Assertions.assertThat(transactions.size() < 5);
    }

    @Test
    void findByFromAccountAndTimestampAfterAndTypeOrType() {
        List<Transaction> startList = repo.findAll();

        //getting filtered list
        String iban = "NL01INHO0000000004";
        LocalDateTime d = LocalDateTime.now().minusHours(24);
        List<Transaction> transactions = repo.findByFromAccountAndTimestampAfterAndTypeOrType(iban, d, 0, 1);

        //Assert that transactions are filtered correctly.
        for(Transaction t : transactions){
            Assertions.assertThat(t.getFromAccount() == iban);
            Assertions.assertThat(LocalDateTime.now().isAfter(t.getTimestamp()) &&
                    LocalDateTime.now().minusHours(24).isBefore(t.getTimestamp()));
        }
        //Assert that filtered is shorter.
        Assertions.assertThat(transactions.size() < startList.size());
    }

    @Test
    void findByFromAccountOrToAccount() {
        String iban = "NL01INHO0000000004";
        Pageable pageable = PageRequest.of(0, 5);
        boolean checkThatListContainsNonMatchingTransactions = false;

        List<Transaction> allTransactions = repo.findAll();
        for(Transaction t : allTransactions){
            if(t.getFromAccount() != iban || t.getToAccount() != iban){
                checkThatListContainsNonMatchingTransactions = true;
            }
        }
        Assertions.assertThat(checkThatListContainsNonMatchingTransactions);

        boolean checkThatListContainsOnlyMatchingTransactions = false;
        List<Transaction> transactions = repo.findByFromAccountOrToAccount(iban, iban, pageable);

        for(Transaction t : transactions){
            if(t.getFromAccount() != iban || t.getToAccount() != iban){
                checkThatListContainsOnlyMatchingTransactions = true;
            }
        }
        Assertions.assertThat(checkThatListContainsOnlyMatchingTransactions == false);
    }
}