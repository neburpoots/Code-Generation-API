package io.swagger.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * UserPatchDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class UserPatchDTO implements DTOEntity {
  @JsonProperty("firstname")
  private String firstname = null;

  @JsonProperty("lastname")
  private String lastname = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("transaction_limit")
  private BigDecimal transactionLimit = null;

  @JsonProperty("daily_limit")
  private BigDecimal dailyLimit = null;

  @JsonProperty("roles")
  private Integer[] roles = null;

  public UserPatchDTO firstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  /**
   * Get firstname
   * @return firstname
   **/
  @Schema(example = "Kiana", description = "")
  
    public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public UserPatchDTO lastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  /**
   * Get lastname
   * @return lastname
   **/
  @Schema(example = "Padilla", description = "")
  
    public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public UserPatchDTO email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @Schema(example = "Kiana.Padilla@gmail.com", description = "")
  
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserPatchDTO transactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  /**
   * Get transactionLimit
   * @return transactionLimit
   **/
  @Schema(example = "100", description = "")
  
    @Valid
    public BigDecimal getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(BigDecimal transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

  public UserPatchDTO dailyLimit(BigDecimal dailyLimit) {
    this.dailyLimit = dailyLimit;
    return this;
  }

  /**
   * Get dailyLimit
   * @return dailyLimit
   **/
  @Schema(example = "25000", description = "")
  
    @Valid
    public BigDecimal getDailyLimit() {
    return dailyLimit;
  }

  public void setDailyLimit(BigDecimal dailyLimit) {
    this.dailyLimit = dailyLimit;
  }

  public UserPatchDTO roles(Integer[] roles) {
    this.roles = roles;
    return this;
  }

  /**
   * Get roles
   * @return roles
   **/
  @Schema(description = "")
  
    @Valid
    public Integer[] getRoles() {
    return roles;
  }

  public void setRoles(Integer[] roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPatchDTO userPatchDTO = (UserPatchDTO) o;
    return Objects.equals(this.firstname, userPatchDTO.firstname) &&
        Objects.equals(this.lastname, userPatchDTO.lastname) &&
        Objects.equals(this.email, userPatchDTO.email) &&
        Objects.equals(this.transactionLimit, userPatchDTO.transactionLimit) &&
        Objects.equals(this.dailyLimit, userPatchDTO.dailyLimit) &&
        Objects.equals(this.roles, userPatchDTO.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstname, lastname, email, transactionLimit, dailyLimit, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserPatchDTO {\n");
    
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
    sb.append("    dailyLimit: ").append(toIndentedString(dailyLimit)).append("\n");
    sb.append("    role: ").append(toIndentedString(roles)).append("\n");
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
