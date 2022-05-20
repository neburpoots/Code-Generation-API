package io.swagger.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

  @NonNull
  private String toAccount;

  @NonNull
  private String fromAccount;

  @NonNull
  private BigDecimal amount;

  @NonNull
  private Integer type;

  @NonNull
  private LocalDateTime timestamp;

  public Transaction(String toAccount, String fromAccount, BigDecimal amount, Integer type){
    this.toAccount = toAccount;
    this.fromAccount = fromAccount;
    this.amount = amount;
    this.type = type;
    this.timestamp = LocalDateTime.now();
  }

  public Transaction(){

  }
  @Override
  public int hashCode() {
    return Objects.hash(toAccount, fromAccount, amount, type, timestamp);
  }
}
