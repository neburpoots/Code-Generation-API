package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository <Transaction, UUID> {

    @Query("SELECT t FROM Transaction t WHERE (:fromAccount is null or t.fromAccount = :fromAccount) " +
            "and (:toAccount is null or t.toAccount = :toAccount)" +
            "and (:timestamp is null or t.date = :timestamp)" +
            "and (:asEq is null or t.amount = :asEq)" +
            "and (:asLt is null or t.amount < :asLt)" +
            "and (:asMt is null or t.amount > :asMt)")
    List<Transaction> filterTransactions(@Param("fromAccount") String fromAccount,
                                         @Param("toAccount") String toAccount,
                                         @Param("timestamp") LocalDate date,
                                         @Param("asEq") BigDecimal asEq,
                                         @Param("asLt") BigDecimal asLt,
                                         @Param("asMt") BigDecimal asMt, Pageable pageable);

    List<Transaction> findByFromAccountAndTimestampAfterAndTypeOrType(String iban, LocalDateTime datetime, Integer payment, Integer withdrawal);

    List<Transaction> findByFromAccountOrToAccount(String iban, String iban2, Pageable pageable);
}