package io.swagger.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * UserPostDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class UserPostDTO implements DTOEntity {
  @JsonProperty("firstname")
  private String firstname = null;

  @JsonProperty("lastname")
  private String lastname = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("password")
  private String password = null;

  public UserPostDTO firstname(String firstname) {
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

  public UserPostDTO lastname(String lastname) {
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

  public UserPostDTO email(String email) {
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

  public UserPostDTO password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   **/
  @Schema(example = "secret123", description = "")

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPostDTO userPostDTO = (UserPostDTO) o;
    return Objects.equals(this.firstname, userPostDTO.firstname) &&
        Objects.equals(this.lastname, userPostDTO.lastname) &&
        Objects.equals(this.email, userPostDTO.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstname, lastname, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserPostDTO {\n");
    
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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
