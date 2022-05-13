package io.swagger.model.account;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.entity.AccountType;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * AccountPostDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class AccountPostDTO implements DTOEntity {
  @JsonProperty("user_id")
  private String user_id = null;

  @JsonProperty("absolute_limit")
  private BigDecimal absoluteLimit = null;

  @JsonProperty("account_type")
  private AccountType accountType = null;

  public AccountPostDTO user_id(String user_id) {
    this.user_id = user_id;
    return this;
  }

  /**
   * Get user_id
   * @return user_id
   **/
  @Schema(example = "123e4567-e89b-12d3-a456-426614174000", description = "")
  
    public String getUser_Id() {
    return user_id;
  }

  public void setUser_Id(String userId) {
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
    
    sb.append("    user_id: ").append(toIndentedString(user_id)).append("\n");
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
