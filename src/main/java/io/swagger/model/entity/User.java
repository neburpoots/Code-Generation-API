package io.swagger.model.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;


@Entity
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @NonNull
  private UUID user_id;

  @NonNull
  private String firstname;

  @NonNull
  private String lastname;

  @NonNull
  @Column(unique=true)
  private String email;

  @NonNull
  private BigDecimal transactionLimit;

  @NonNull
  private BigDecimal dailyLimit;

  @NonNull
  private String password;

  @Override
  public int hashCode(){
    return user_id.hashCode() * firstname.hashCode() * lastname.hashCode();
  }

  public void setRolesForUser(List<Role> roles) {
    Set<Role> newRoles = new HashSet<>(roles);
    this.setRoles(newRoles);
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles = new HashSet<>();

  public User(String firstname, String lastname, String email, BigDecimal transactionLimit, BigDecimal dailyLimit, String password) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.transactionLimit = transactionLimit;
    this.dailyLimit = dailyLimit;
    this.password = password;
  }

  public User() {
  }
}
