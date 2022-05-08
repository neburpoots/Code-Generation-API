package io.swagger.model.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;


@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

  @Id
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

}
