package io.swagger.model.transaction;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.entity.TransactionType;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import org.threeten.bp.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TransactionPostDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")

public class TransactionPostDTO implements DTOEntity {
  @JsonProperty("toAccount")
  @Pattern(regexp="[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{8,14}",message="Iban of the to account was in invalid form.")
  @NotBlank(message = "IBAN of to account has to be entered.")
  private String toAccount;

  @JsonProperty("fromAccount")
  @Pattern(regexp="[A-Z]{2}[0-9]{2}[A-Z]{4}[0-9]{8,14}",message="Iban of the from account was in invalid form.")
  @NotBlank(message = "IBAN of from account has to be entered.")
  private String fromAccount;

  @JsonProperty("amount")
  @DecimalMin(value = "0.01", message = "Transaction amount can not be lower than (0.01). ")
  @NotNull(message = "Transaction amount must be entered. ")
  private BigDecimal amount;

  @JsonProperty("transaction_type")
  @NonNull
  private TransactionType type;

  @JsonProperty("timestamp")
  private LocalDateTime timestamp = LocalDateTime.now();

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public TransactionPostDTO toAccount(String toAccount) {
    this.toAccount = toAccount;
    return this;
  }

  /**
   * Get toAccount
   * @return toAccount
   **/
  @Schema(example = "NL41INHO0546284337", description = "")

  public String getToAccount() {
    return toAccount;
  }

  public void setToAccount(String toAccount) {
    this.toAccount = toAccount;
  }

  public TransactionPostDTO fromAccount(String fromAccount) {
    this.fromAccount = fromAccount;
    return this;
  }
  /**
   * Get fromAccount
   * @return fromAccount
   **/
  @Schema(example = "NL43INHO0348271748", description = "")
  
    public String getFromAccount() {
    return fromAccount;
  }

  public void setFromAccount(String fromAccount) {
    this.fromAccount = fromAccount;
  }

  public TransactionPostDTO amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }
  /**
   * Get amount
   * @return amount
   **/
  @Schema(example = "250", description = "")
  
    @Valid
    public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /**
   * Get type
   * @return type
   **/

    public TransactionType getType() {
    return this.type;
  }

  public void setType(TransactionType type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(toAccount, fromAccount, amount, type);
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
