package io.swagger.model.entity;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;

/**
 * User
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User   {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID user_id;

  private String firstname;

  private String lastname;

  private String email;

  private BigDecimal transactionLimit;

  private BigDecimal dailyLimit;

  @ManyToMany
  @JoinTable(
          name = "user_role",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  Set<Role> Roles;
}
