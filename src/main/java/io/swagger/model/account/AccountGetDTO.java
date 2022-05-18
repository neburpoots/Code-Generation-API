package io.swagger.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.entity.AccountType;
import io.swagger.model.user.UserGetDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class AccountGetDTO {

    @JsonProperty("account_id")
    private String account_id = null;

    @JsonProperty("user")
    private UserGetDTO user = null;

    @JsonProperty("balance")
    private BigDecimal balance = null;

    @JsonProperty("absoluteLimit")
    private BigDecimal absoluteLimit = null;

    @JsonProperty("accountType")
    private AccountType accountType = null;

    @JsonProperty("status")
    private Boolean status = null;

    /**
     * Get account_id
     * @return account_id
     **/
    @Schema(example = "NL69SWAG0123456789", description = "")

    @Valid
    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public AccountGetDTO account_id(UserGetDTO user) {
        this.user = user;
        return this;
    }

    /**
     * Get user
     * @return user
     **/
    @Schema(example = "user", description = "")

    public UserGetDTO getuser() {
        return user;
    }

    public void setUser(UserGetDTO user) {
        this.user = user;
    }

    public AccountGetDTO user(UserGetDTO user) {
        this.user = user;
        return this;
    }

    /**
     * Get balance
     * @return balance
     **/
    @Schema(example = "30.00", description = "")

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountGetDTO balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    /**
     * Get absoluteLimit
     * @return absoluteLimit
     **/
    @Schema(example = "-500", description = "")

    public BigDecimal getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    public AccountGetDTO absoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
        return this;
    }

    /**
     * Get get accountType
     * @return accountType
     **/
    @Schema(example = "PRIMARY", description = "")

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountGetDTO accountType(AccountType accountType) {
        this.accountType = accountType;
        return this;
    }

    /**
     * Get get status
     * @return status
     **/
    @Schema(example = "true", description = "")

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AccountGetDTO status(Boolean status) {
        this.status = status;
        return this;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountGetDTO AccountGetDTO = (AccountGetDTO) o;
        return Objects.equals(this.account_id, AccountGetDTO.account_id) &&
                Objects.equals(this.user, AccountGetDTO.user) &&
                Objects.equals(this.balance, AccountGetDTO.balance) &&
                Objects.equals(this.absoluteLimit, AccountGetDTO.absoluteLimit) &&
                Objects.equals(this.accountType, AccountGetDTO.accountType) &&
                Objects.equals(this.status, AccountGetDTO.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account_id, user, balance, absoluteLimit, accountType, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserGetDTO {\n");

        sb.append("    account_id: ").append(toIndentedString(account_id)).append("\n");
        sb.append("    user: ").append(toIndentedString(user)).append("\n");
        sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
        sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
        sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("}");
        return sb.toString();
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
