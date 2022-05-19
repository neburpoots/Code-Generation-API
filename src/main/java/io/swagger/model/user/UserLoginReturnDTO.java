package io.swagger.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.entity.Role;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * UserGetDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")


public class UserLoginReturnDTO implements DTOEntity {
    @JsonProperty("user_id")
    private UUID user_id = null;

    @JsonProperty("firstname")
    private String firstname = null;

    @JsonProperty("lastname")
    private String lastname = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("transaction_limit")
    private BigDecimal transactionLimit;

    @JsonProperty("daily_limit")
    private BigDecimal dailyLimit;

    @JsonProperty("role")
    private List<Role> roles;

    @JsonProperty("access_token")
    private String accessToken;

    public UserLoginReturnDTO user_id(UUID user_id) {
        this.user_id = user_id;
        return this;
    }

    /**
     * Get user_id
     * @return user_id
     **/
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", description = "")

    @Valid
    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UserLoginReturnDTO firstname(String firstname) {
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

    public UserLoginReturnDTO lastname(String lastname) {
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

    public UserLoginReturnDTO email(String email) {
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

    public UserLoginReturnDTO transactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
        return this;
    }

    /**
     * Get transactionLimit
     * @return transactionLimit
     **/
    @Schema(example = "150", description = "")

    public BigDecimal getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public UserLoginReturnDTO dailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
        return this;
    }

    /**
     * Get dailyLimit
     * @return dailyLimit
     **/
    @Schema(example = "2500", description = "")

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public UserLoginReturnDTO accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * Get accessToken
     * @return accessToken
     **/
    @Schema(example = "", description = "")

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserLoginReturnDTO role(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    /**
     * Get roles
     * @return roles
     **/
    @Schema(example = "", description = "")

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserLoginReturnDTO userLoginReturnDTO = (UserLoginReturnDTO) o;
        return Objects.equals(this.user_id, userLoginReturnDTO.user_id) &&
                Objects.equals(this.firstname, userLoginReturnDTO.firstname) &&
                Objects.equals(this.lastname, userLoginReturnDTO.lastname) &&
                Objects.equals(this.email, userLoginReturnDTO.email) &&
                Objects.equals(this.transactionLimit, userLoginReturnDTO.transactionLimit) &&
                Objects.equals(this.dailyLimit, userLoginReturnDTO.dailyLimit) &&
                Objects.equals(this.accessToken, userLoginReturnDTO.accessToken) &&
                Objects.equals(this.roles, userLoginReturnDTO.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, firstname, lastname, email, transactionLimit, dailyLimit, roles, accessToken);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserLoginReturnDTO {\n");

        sb.append("    user_id: ").append(toIndentedString(user_id)).append("\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
        sb.append("    dailyLimit: ").append(toIndentedString(dailyLimit)).append("\n");
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
