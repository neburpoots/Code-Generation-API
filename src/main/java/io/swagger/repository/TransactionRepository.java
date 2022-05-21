package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository <Transaction, UUID> {
    //returns all transactions with to_accounts matching the given iban.
    List<Transaction> findByToAccount(String iban);

    //returns all transactions with from_accounts matching the given iban.
    List<Transaction> findByFromAccount(String iban);

    List<Transaction> findByToAccountAndFromAccount(String toAccount, String fromAccount);
}
