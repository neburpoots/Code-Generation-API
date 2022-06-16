package io.swagger.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

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
                    name = "sequence_prefix", value = "NL01")
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
