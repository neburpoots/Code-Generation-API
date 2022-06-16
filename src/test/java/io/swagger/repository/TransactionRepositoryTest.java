package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

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