package io.swagger.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.utils.DTOEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

/**
 * UserSearchDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-05T18:12:07.854Z[GMT]")

public class UserIbanSearchDTO implements DTOEntity {

    public UserIbanSearchDTO(UUID user_id, String firstname, String lastname, String email, String iban)
    {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.iban = iban;
    }

    public UserIbanSearchDTO()
    {
    }

    @JsonProperty("user_id")
    private UUID user_id = null;

    @JsonProperty("firstname")
    private String firstname = null;

    @JsonProperty("lastname")
    private String lastname = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("iban")
    private String iban = null;

    public UserIbanSearchDTO user_id(UUID user_id) {
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

    public UserIbanSearchDTO firstname(String firstname) {
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

    public UserIbanSearchDTO lastname(String lastname) {
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

    public UserIbanSearchDTO email(String email) {
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

    public UserIbanSearchDTO iban(String iban) {
        this.iban = iban;
        return this;
    }

    /**
     * Get iban
     * @return iban
     **/
    @Schema(example = "NL69SWAG0000000000", description = "")

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserIbanSearchDTO userGetDTO = (UserIbanSearchDTO) o;
        return Objects.equals(this.user_id, userGetDTO.user_id) &&
                Objects.equals(this.firstname, userGetDTO.firstname) &&
                Objects.equals(this.lastname, userGetDTO.lastname) &&
                Objects.equals(this.email, userGetDTO.email) &&
                Objects.equals(this.iban, userGetDTO.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, firstname, lastname, email);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserSearchDTO {\n");

        sb.append("    user_id: ").append(toIndentedString(user_id)).append("\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
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
