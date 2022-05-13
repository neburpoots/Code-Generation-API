package io.swagger.model.user;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * UserPasswordDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class UserPasswordDTO implements DTOEntity {
  @JsonProperty("currentPassword")
  private String currentPassword = null;

  @JsonProperty("newPassword")
  private String newPassword = null;

  @JsonProperty("newPasswordConfirm")
  private String newPasswordConfirm = null;

  public UserPasswordDTO currentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
    return this;
  }

  /**
   * Get currentPassword
   * @return currentPassword
   **/
  @Schema(example = "Kiana123", description = "")
  
    public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public UserPasswordDTO newPassword(String newPassword) {
    this.newPassword = newPassword;
    return this;
  }

  /**
   * Get newPassword
   * @return newPassword
   **/
  @Schema(example = "Padilla123", description = "")
  
    public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public UserPasswordDTO newPasswordConfirm(String newPasswordConfirm) {
    this.newPasswordConfirm = newPasswordConfirm;
    return this;
  }

  /**
   * Get newPasswordConfirm
   * @return newPasswordConfirm
   **/
  @Schema(example = "Padilla123", description = "")
  
    public String getNewPasswordConfirm() {
    return newPasswordConfirm;
  }

  public void setNewPasswordConfirm(String newPasswordConfirm) {
    this.newPasswordConfirm = newPasswordConfirm;
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
    return Objects.equals(this.currentPassword, userPasswordDTO.currentPassword) &&
        Objects.equals(this.newPassword, userPasswordDTO.newPassword) &&
        Objects.equals(this.newPasswordConfirm, userPasswordDTO.newPasswordConfirm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentPassword, newPassword, newPasswordConfirm);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserPasswordDTO {\n");
    
    sb.append("    currentPassword: ").append(toIndentedString(currentPassword)).append("\n");
    sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
    sb.append("    newPasswordConfirm: ").append(toIndentedString(newPasswordConfirm)).append("\n");
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
