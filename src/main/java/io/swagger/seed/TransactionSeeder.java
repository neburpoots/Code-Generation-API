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

        Transaction t = new Transaction(accounts.get(0).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(30), TransactionType.regular_transaction);
        Transaction t2 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(3).getAccount_id(), new BigDecimal(10), TransactionType.withdrawal);
        Transaction t3 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(5).getAccount_id(), new BigDecimal(80), TransactionType.withdrawal);
        Transaction t4 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(5).getAccount_id(), new BigDecimal(80), TransactionType.regular_transaction);
        Transaction t5 = new Transaction(accounts.get(0).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(30), TransactionType.deposit);
        Transaction t6 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(10), TransactionType.regular_transaction);
        Transaction t7 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(5).getAccount_id(), new BigDecimal(80), TransactionType.deposit);
        Transaction t8 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(3).getAccount_id(), new BigDecimal(0), TransactionType.regular_transaction);
        Transaction t9 = new Transaction(accounts.get(0).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(30), TransactionType.withdrawal);
        Transaction t10 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(10), TransactionType.regular_transaction);
        Transaction t11 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(0).getAccount_id(), new BigDecimal(80), TransactionType.deposit);
        Transaction t12 = new Transaction(accounts.get(1).getAccount_id(), accounts.get(1).getAccount_id(), new BigDecimal(80), TransactionType.regular_transaction);

        return this.transactionRepo.saveAll(
                List.of(t, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12)
        );
    }
}
