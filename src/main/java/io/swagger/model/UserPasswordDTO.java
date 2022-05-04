package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserPasswordDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T19:02:55.812Z[GMT]")


public class UserPasswordDTO   {
  @JsonProperty("oldPassword")
  private String oldPassword = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("repeatPassword")
  private String repeatPassword = null;

  public UserPasswordDTO oldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
    return this;
  }

  /**
   * Get oldPassword
   * @return oldPassword
   **/
  @Schema(example = "P422w0rd123", required = true, description = "")
      @NotNull

    public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public UserPasswordDTO password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   **/
  @Schema(example = "newP422w0rd432", required = true, description = "")
      @NotNull

    public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserPasswordDTO repeatPassword(String repeatPassword) {
    this.repeatPassword = repeatPassword;
    return this;
  }

  /**
   * Get repeatPassword
   * @return repeatPassword
   **/
  @Schema(example = "newP422w0rd432", required = true, description = "")
      @NotNull

    public String getRepeatPassword() {
    return repeatPassword;
  }

  public void setRepeatPassword(String repeatPassword) {
    this.repeatPassword = repeatPassword;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPasswordDTO userPasswordDTO = (UserPasswordDTO) o;
    return Objects.equals(this.oldPassword, userPasswordDTO.oldPassword) &&
        Objects.equals(this.password, userPasswordDTO.password) &&
        Objects.equals(this.repeatPassword, userPasswordDTO.repeatPassword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oldPassword, password, repeatPassword);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserPasswordDTO {\n");
    
    sb.append("    oldPassword: ").append(toIndentedString(oldPassword)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    repeatPassword: ").append(toIndentedString(repeatPassword)).append("\n");
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
