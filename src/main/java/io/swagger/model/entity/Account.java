package io.swagger.model.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * Account
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Account   {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID account_id;

  @NonNull
  private BigDecimal balance;

  @NonNull
  private BigDecimal absoluteLimit;

  @NonNull
  private AccountType type;

  @NonNull
  private Boolean status;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name="user_id", referencedColumnName = "user_id",nullable=false)
  private User user;
}
