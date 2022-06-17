package io.swagger.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * AccountPatchDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class AccountPatchDTO implements DTOEntity {

  @JsonProperty("status")
  @Nullable
  private Boolean status = null;

  @JsonProperty("absolute_limit")
  @DecimalMin(value = "-10000.01", inclusive = false)
  @DecimalMax(value = "0.01", inclusive = false)
  @Digits(integer=5, fraction=2)
  @Nullable
  private BigDecimal absoluteLimit = null;

  public AccountPatchDTO absoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
    return this;
  }

  /**
   * Get absoluteLimit
   * @return absoluteLimit
   **/
  @Schema(example = "-1500", description = "")
  
    @Valid
    public BigDecimal getAbsoluteLimit() {
    return absoluteLimit;
  }

  public void setAbsoluteLimit(BigDecimal absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
  }

  public AccountPatchDTO status(Boolean status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   **/
  @Schema(example = "false", description = "")
  
    public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountPatchDTO accountPatchDTO = (AccountPatchDTO) o;
    return Objects.equals(this.absoluteLimit, accountPatchDTO.absoluteLimit) &&
        Objects.equals(this.status, accountPatchDTO.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(absoluteLimit, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountPatchDTO {\n");
    
    sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
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
