package io.swagger.model.entity;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Getter
@Setter
public class Transaction   {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @NonNull
  private UUID transaction_id;

  @ManyToOne
  @JoinColumn(name = "to_account_account_id")
  @NonNull
  private Account toAccount;

  @ManyToOne
  @JoinColumn(name = "from_account_account_id")
  @NonNull
  private Account fromAccount;

  @NonNull
  private BigDecimal amount;

  @NonNull
  private TransactionType type;

  @NonNull
  private String timestamp;

  public Transaction(Account toAccount, Account fromAccount, BigDecimal amount, TransactionType type, String timestamp){
    this.toAccount = toAccount;
    this.fromAccount = fromAccount;
    this.amount = amount;
    this.type = type;
    this.timestamp = timestamp;
  }

  public Transaction(){

  }
  @Override
  public int hashCode() {
    return Objects.hash(toAccount, fromAccount, amount, type, timestamp);
  }
}
