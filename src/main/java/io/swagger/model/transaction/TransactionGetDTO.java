package io.swagger.model.transaction;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.entity.TransactionType;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.threeten.bp.LocalDateTime;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * TransactionPostDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class TransactionGetDTO implements DTOEntity {
    public UUID getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(UUID transaction_id) {
        this.transaction_id = transaction_id;
    }

    @JsonProperty("transaction_id")
    private UUID transaction_id;

    @JsonProperty("toAccount")
    private String toAccount = null;

    @JsonProperty("fromAccount")
    private String fromAccount = null;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("transaction_type")
    private TransactionType type = null;


    public TransactionGetDTO toAccount(String toAccount) {
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

    public TransactionGetDTO fromAccount(String fromAccount) {
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

    public TransactionGetDTO amount(BigDecimal amount) {
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
    @Schema(description = "")

    public TransactionType getType() {
        return type;
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
