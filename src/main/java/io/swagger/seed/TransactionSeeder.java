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

        Transaction t = new Transaction(accounts.get(0).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(330), 1);
        Transaction t2 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(210), 2);
        Transaction t3 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(0).getAccount_id(), new BigDecimal(480), 1);
        Transaction t4 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(80), 0);

        //List<Transaction> transactions
        return (List<Transaction>)this.transactionRepo.saveAll(
                List.of(t, t2, t3, t4)
        );
    }
}
