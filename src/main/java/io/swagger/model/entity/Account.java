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
public class Account implements Identifiable<String>  {

  @Id
  @GenericGenerator(
          name = "assigned-sequence",
          strategy = "io.swagger.model.entity.StringSequenceIdentifier",
          parameters = {
            @org.hibernate.annotations.Parameter(
                    name = "sequence_name", value = "hibernate_sequence"),
            @org.hibernate.annotations.Parameter(
                    name = "sequence_prefix", value = "NL69")
      }
  )
  @GeneratedValue(
          generator = "assigned-sequence",
          strategy = GenerationType.SEQUENCE)
  private String account_id;

  @Override
  public String getId() {
    return account_id;
  }

  @NonNull
  private BigDecimal balance;

  @NonNull
  private BigDecimal absoluteLimit;

  @NonNull
  private AccountType accountType;

  @NonNull
  private Boolean status;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name="user_id", referencedColumnName = "user_id",nullable=false)
  private User user;
}
