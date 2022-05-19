package io.swagger.seed;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.TransactionType;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TransactionSeeder {
    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;
    @Autowired
    public TransactionSeeder(TransactionRepository transactionRepo, AccountRepository accountRepo){
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public List <Transaction> seed(){
        List<Account> accounts = this.accountRepo.findAll();

        Transaction t = new Transaction(accounts.get(0), accounts.get(1), new BigDecimal(330), TransactionType.to_primary, "12-12-2022");
        Transaction t2 = new Transaction(accounts.get(1), accounts.get(1), new BigDecimal(210), TransactionType.withdrawal, "12-12-2022");
        Transaction t3 = new Transaction(accounts.get(1), accounts.get(0), new BigDecimal(480), TransactionType.to_saving, "12-12-2022");
        Transaction t4 = new Transaction(accounts.get(1), accounts.get(1), new BigDecimal(80), TransactionType.to_primary, "12-12-2022");

        //List<Transaction> transactions
        return (List<Transaction>)this.transactionRepo.saveAll(
                List.of(t, t2, t3, t4)
        );
    }
}
