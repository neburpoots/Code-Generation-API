package io.swagger.model.entity;

import java.util.Objects;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.threeten.bp.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
public class Transaction   {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @NonNull
  private UUID transaction_id;

  @NonNull
  private String toAccount;

  @NonNull
  private String fromAccount;

  @NonNull
  @Min(value = 0, message = "Entered amount can not be below 0.01.")
  private BigDecimal amount;

  @NonNull
  private LocalDateTime timestamp;

  @NonNull
  private TransactionType transactionType;

  public Transaction(String toAccount, String fromAccount, BigDecimal amount, TransactionType transactionType){
    this.toAccount = toAccount;
    this.fromAccount = fromAccount;
    this.amount = amount;
    this.transactionType = transactionType;
    this.timestamp = LocalDateTime.now();
  }

  public Transaction(){

  }
  @Override
  public int hashCode() {
    return Objects.hash(toAccount, fromAccount, amount, transactionType, timestamp);
  }
}
