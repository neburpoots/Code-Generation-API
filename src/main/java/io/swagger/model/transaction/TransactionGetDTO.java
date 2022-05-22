package io.swagger.model.transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import lombok.NonNull;
import org.apache.tomcat.jni.Local;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * TransactionPostDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class TransactionGetDTO implements DTOEntity {
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

    @JsonProperty("type")
    private Integer type = null;

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

    public TransactionGetDTO type(Integer type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     * @return type
     **/
    @Schema(description = "")

    @Valid
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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
