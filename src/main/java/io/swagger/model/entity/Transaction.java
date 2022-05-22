package io.swagger.model.entity;

import java.util.Objects;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.threeten.bp.LocalDate;

import javax.persistence.*;

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
  private BigDecimal amount;

  @NonNull
  private Integer type;

  @NonNull
  private LocalDate timestamp;

  public Transaction(String toAccount, String fromAccount, BigDecimal amount, Integer type){
    this.toAccount = toAccount;
    this.fromAccount = fromAccount;
    this.amount = amount;
    this.type = type;
    this.timestamp = LocalDate.now();
  }

  public Transaction(){

  }
  @Override
  public int hashCode() {
    return Objects.hash(toAccount, fromAccount, amount, type, timestamp);
  }
}
