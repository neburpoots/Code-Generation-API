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
  private UUID user_id;

  private String firstname;

  private String lastname;

  private String email;

  private Integer transactionLimit;

  private BigDecimal dailyLimit;

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

  @ManyToMany
  private Set<Role> roles = new HashSet<>();

  public User(String firstname, String lastname, String email, Integer transactionLimit, BigDecimal dailyLimit, String password) {
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
