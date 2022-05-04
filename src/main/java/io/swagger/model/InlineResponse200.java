package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InlineResponse200
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T19:02:55.812Z[GMT]")


public class InlineResponse200   {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("user")
  private UserDTO user = null;

  public InlineResponse200 description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   **/
  @Schema(example = "Successfully executed request", description = "")
  
    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public InlineResponse200 user(UserDTO user) {
    this.user = user;
    return this;
  }

  /**
   * Get user
   * @return user
   **/
  @Schema(description = "")
  
    @Valid
    public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse200 inlineResponse200 = (InlineResponse200) o;
    return Objects.equals(this.description, inlineResponse200.description) &&
        Objects.equals(this.user, inlineResponse200.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, user);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse200 {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
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
