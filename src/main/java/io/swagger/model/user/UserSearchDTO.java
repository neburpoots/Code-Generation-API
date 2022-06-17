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

public class UserSearchDTO implements DTOEntity {

    public UserSearchDTO(UUID user_id, String firstname, String lastname, String email)
    {
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public UserSearchDTO()
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

    public UserSearchDTO user_id(UUID user_id) {
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

    public UserSearchDTO firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    /**
     * Get firstname
     * @return firstname
     **/
    @Schema(example = "Ruben", description = "")

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public UserSearchDTO lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    /**
     * Get lastname
     * @return lastname
     **/
    @Schema(example = "Stoop", description = "")

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public UserSearchDTO email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get email
     * @return email
     **/
    @Schema(example = "ruben@student.inholland.nl", description = "")

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSearchDTO userGetDTO = (UserSearchDTO) o;
        return Objects.equals(this.user_id, userGetDTO.user_id) &&
                Objects.equals(this.firstname, userGetDTO.firstname) &&
                Objects.equals(this.lastname, userGetDTO.lastname) &&
                Objects.equals(this.email, userGetDTO.email);
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
