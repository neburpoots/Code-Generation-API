package io.swagger.model.entity;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Formula;

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

  public String getFullName() {
    return getFirstname().concat(" ").concat(getLastname());
  }

  @Override
  public int hashCode(){
    return user_id.hashCode() * firstname.hashCode() * lastname.hashCode();
  }

  public void setRolesForUser(List<Role> roles) {
    List<Role> newRoles = new ArrayList<>(roles);
    this.setRoles(newRoles);
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Role> roles = new ArrayList<>();

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
