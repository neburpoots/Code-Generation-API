package io.swagger.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.entity.AccountType;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * AccountPostDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class AccountPostDTO implements DTOEntity {

  @JsonProperty("user_id")
  @NotNull
  private UUID user_id = null;

  @JsonProperty("absolute_limit")
  @NotNull
  @DecimalMin(value = "-10000.01", inclusive = false)
  @DecimalMax(value = "0.01", inclusive = false)
  @Digits(integer=5, fraction=2)
  private BigDecimal absoluteLimit = null;

  @JsonProperty("account_type")
  @NotNull
  private AccountType accountType = null;

  public AccountPostDTO user_id(UUID user_Id) {
    this.user_id = user_Id;
    return this;
  }

  /**
   * Get user_id
   * @return user_id
   **/
  @Schema(example = "123e4567-e89b-12d3-a456-426614174000", description = "")

  @Valid
    public UUID getUser_id() {
    return user_id;
  }

  public void setUser_id(UUID user_id) {
    this.user_id = user_id;
  }

  public AccountPostDTO absoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
    return this;
  }

  /**
   * Get absoluteLimit
   * @return absoluteLimit
   **/
  @Schema(example = "-1000", description = "")
  
    @Valid
    public BigDecimal getAbsoluteLimit() {
    return absoluteLimit;
  }

  public void setAbsoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
  }

  /**
   * Get account type
   * @return account type
   **/
  @Schema(example = "PRIMARY", description = "")

  @Valid
  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }


  public AccountPostDTO accountType(AccountType accountType) {
    this.accountType = accountType;
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
    AccountPostDTO accountPostDTO = (AccountPostDTO) o;
    return Objects.equals(this.user_id, accountPostDTO.user_id) &&
            Objects.equals(this.accountType, accountPostDTO.accountType) &&
        Objects.equals(this.absoluteLimit, accountPostDTO.absoluteLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user_id, absoluteLimit, accountType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountPostDTO {\n");
    
    sb.append("    user_Id: ").append(toIndentedString(user_id)).append("\n");
    sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
    sb.append("    account_type: ").append(toIndentedString(accountType)).append("\n");
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
