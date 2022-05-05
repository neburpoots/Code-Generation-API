package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Pagination
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T17:27:41.112Z[GMT]")


public class Pagination   {
  @JsonProperty("current_page")
  private BigDecimal currentPage = null;

  @JsonProperty("next_page")
  private BigDecimal nextPage = null;

  @JsonProperty("prev_page")
  private BigDecimal prevPage = null;

  @JsonProperty("last_page")
  private BigDecimal lastPage = null;

  @JsonProperty("total_items")
  private BigDecimal totalItems = null;

  public Pagination currentPage(BigDecimal currentPage) {
    this.currentPage = currentPage;
    return this;
  }

  /**
   * Get currentPage
   * @return currentPage
   **/
  @Schema(example = "1", description = "")
  
    @Valid
    public BigDecimal getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(BigDecimal currentPage) {
    this.currentPage = currentPage;
  }

  public Pagination nextPage(BigDecimal nextPage) {
    this.nextPage = nextPage;
    return this;
  }

  /**
   * Get nextPage
   * @return nextPage
   **/
  @Schema(example = "2", description = "")
  
    @Valid
    public BigDecimal getNextPage() {
    return nextPage;
  }

  public void setNextPage(BigDecimal nextPage) {
    this.nextPage = nextPage;
  }

  public Pagination prevPage(BigDecimal prevPage) {
    this.prevPage = prevPage;
    return this;
  }

  /**
   * Get prevPage
   * @return prevPage
   **/
  @Schema(example = "0", description = "")
  
    @Valid
    public BigDecimal getPrevPage() {
    return prevPage;
  }

  public void setPrevPage(BigDecimal prevPage) {
    this.prevPage = prevPage;
  }

  public Pagination lastPage(BigDecimal lastPage) {
    this.lastPage = lastPage;
    return this;
  }

  /**
   * Get lastPage
   * @return lastPage
   **/
  @Schema(example = "7", description = "")
  
    @Valid
    public BigDecimal getLastPage() {
    return lastPage;
  }

  public void setLastPage(BigDecimal lastPage) {
    this.lastPage = lastPage;
  }

  public Pagination totalItems(BigDecimal totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  /**
   * Get totalItems
   * @return totalItems
   **/
  @Schema(example = "73", description = "")
  
    @Valid
    public BigDecimal getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(BigDecimal totalItems) {
    this.totalItems = totalItems;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pagination pagination = (Pagination) o;
    return Objects.equals(this.currentPage, pagination.currentPage) &&
        Objects.equals(this.nextPage, pagination.nextPage) &&
        Objects.equals(this.prevPage, pagination.prevPage) &&
        Objects.equals(this.lastPage, pagination.lastPage) &&
        Objects.equals(this.totalItems, pagination.totalItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentPage, nextPage, prevPage, lastPage, totalItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pagination {\n");
    
    sb.append("    currentPage: ").append(toIndentedString(currentPage)).append("\n");
    sb.append("    nextPage: ").append(toIndentedString(nextPage)).append("\n");
    sb.append("    prevPage: ").append(toIndentedString(prevPage)).append("\n");
    sb.append("    lastPage: ").append(toIndentedString(lastPage)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
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
