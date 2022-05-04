package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.AccountDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InlineResponse2004
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T19:02:55.812Z[GMT]")


public class InlineResponse2004   {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("account")
  private AccountDTO account = null;

  public InlineResponse2004 description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   **/
  @Schema(example = "Successfully retrieved account", description = "")
  
    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public InlineResponse2004 account(AccountDTO account) {
    this.account = account;
    return this;
  }

  /**
   * Get account
   * @return account
   **/
  @Schema(description = "")
  
    @Valid
    public AccountDTO getAccount() {
    return account;
  }

  public void setAccount(AccountDTO account) {
    this.account = account;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2004 inlineResponse2004 = (InlineResponse2004) o;
    return Objects.equals(this.description, inlineResponse2004.description) &&
        Objects.equals(this.account, inlineResponse2004.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, account);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2004 {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    account: ").append(toIndentedString(account)).append("\n");
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
