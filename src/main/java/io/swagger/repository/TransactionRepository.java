package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository <Transaction, UUID> {

    @Query("SELECT t FROM Transaction t WHERE (:fromAccount is null or t.fromAccount = :fromAccount) " +
            "and (:toAccount is null or t.toAccount = :toAccount)" +
            "and (:fromDate is null or t.timestamp > :fromDate)" +
            "and (:untilDate is null or t.timestamp < :untilDate)" +
            "and (:asEq is null or t.amount = :asEq)" +
            "and (:asLt is null or t.amount < :asLt)" +
            "and (:asMt is null or t.amount > :asMt)")
    List<Transaction> filterTransactions(@Param("fromAccount") String fromAccount,
                                         @Param("toAccount") String toAccount,
                                         @Param("fromDate") LocalDateTime fromDate,
                                         @Param("untilDate") LocalDateTime untilDate,
                                         @Param("asEq") BigDecimal asEq,
                                         @Param("asLt") BigDecimal asLt,
                                         @Param("asMt") BigDecimal asMt, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :fromAccount or t.toAccount = :fromAccount) " +
            "and (:toAccount is null or t.toAccount = :toAccount)" +
            "and (:fromDate is null or t.timestamp > :fromDate)" +
            "and (:untilDate is null or t.timestamp < :untilDate)" +
            "and (:asEq is null or t.amount = :asEq)" +
            "and (:asLt is null or t.amount < :asLt)" +
            "and (:asMt is null or t.amount > :asMt)")
    List<Transaction> filterTransactionsForCustomer(@Param("fromAccount") String fromAccount,
                                                    @Param("toAccount") String toAccount,
                                                    @Param("fromDate") LocalDateTime fromDate,
                                                    @Param("untilDate") LocalDateTime untilDate,
                                                    @Param("asEq") BigDecimal asEq,
                                                    @Param("asLt") BigDecimal asLt,
                                                    @Param("asMt") BigDecimal asMt, Pageable pageable);

    List<Transaction> findByFromAccountOrToAccount(String iban, String iban2, Pageable pageable);

    //Returns sum of all the transactions that were made within the last 24 hours, with the provided IBAN.
    @Query("SELECT sum(transaction.amount) FROM Transaction  transaction WHERE (transaction.fromAccount = :fromAccount) and (transaction.timestamp > :oneDayAgo)")
    BigDecimal getTransactionsAmountForIbanFromLastDay(@Param("fromAccount") String fromAccount, @Param("oneDayAgo") LocalDateTime oneDayAgo);
}