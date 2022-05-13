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
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

  @Id
  @Column(name = "user_id", unique = true)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID user_id;

  @NonNull
  private String firstname;

  @NonNull
  private String lastname;

  @NonNull
  @Column(name = "email", unique = true)
  private String email;

  @NonNull
  private Integer transactionLimit;

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

  @ManyToMany(fetch = FetchType.EAGER,
          cascade = {
                  CascadeType.MERGE
          })
  @JoinTable(name = "user_role",
          joinColumns = { @JoinColumn(name = "user_id") },
          inverseJoinColumns = { @JoinColumn(name = "role_id") })
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy="user")
  @JsonIgnore
  private Set<Account> accounts = new HashSet<>();
}
