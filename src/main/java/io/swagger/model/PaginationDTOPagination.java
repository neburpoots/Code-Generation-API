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
 * PaginationDTOPagination
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T19:02:55.812Z[GMT]")


public class PaginationDTOPagination   {
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

  public PaginationDTOPagination currentPage(BigDecimal currentPage) {
    this.currentPage = currentPage;
    return this;
  }

  /**
   * Get currentPage
   * @return currentPage
   **/
  @Schema(example = "2", description = "")
  
    @Valid
    public BigDecimal getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(BigDecimal currentPage) {
    this.currentPage = currentPage;
  }

  public PaginationDTOPagination nextPage(BigDecimal nextPage) {
    this.nextPage = nextPage;
    return this;
  }

  /**
   * Get nextPage
   * @return nextPage
   **/
  @Schema(example = "3", description = "")
  
    @Valid
    public BigDecimal getNextPage() {
    return nextPage;
  }

  public void setNextPage(BigDecimal nextPage) {
    this.nextPage = nextPage;
  }

  public PaginationDTOPagination prevPage(BigDecimal prevPage) {
    this.prevPage = prevPage;
    return this;
  }

  /**
   * Get prevPage
   * @return prevPage
   **/
  @Schema(example = "1", description = "")
  
    @Valid
    public BigDecimal getPrevPage() {
    return prevPage;
  }

  public void setPrevPage(BigDecimal prevPage) {
    this.prevPage = prevPage;
  }

  public PaginationDTOPagination lastPage(BigDecimal lastPage) {
    this.lastPage = lastPage;
    return this;
  }

  /**
   * Get lastPage
   * @return lastPage
   **/
  @Schema(example = "10", description = "")
  
    @Valid
    public BigDecimal getLastPage() {
    return lastPage;
  }

  public void setLastPage(BigDecimal lastPage) {
    this.lastPage = lastPage;
  }

  public PaginationDTOPagination totalItems(BigDecimal totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  /**
   * Get totalItems
   * @return totalItems
   **/
  @Schema(example = "455", required = true, description = "")
      @NotNull

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
    PaginationDTOPagination paginationDTOPagination = (PaginationDTOPagination) o;
    return Objects.equals(this.currentPage, paginationDTOPagination.currentPage) &&
        Objects.equals(this.nextPage, paginationDTOPagination.nextPage) &&
        Objects.equals(this.prevPage, paginationDTOPagination.prevPage) &&
        Objects.equals(this.lastPage, paginationDTOPagination.lastPage) &&
        Objects.equals(this.totalItems, paginationDTOPagination.totalItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentPage, nextPage, prevPage, lastPage, totalItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginationDTOPagination {\n");
    
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
