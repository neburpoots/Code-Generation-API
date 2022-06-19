package io.swagger.model.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
