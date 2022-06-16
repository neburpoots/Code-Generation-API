package io.swagger.model.entity;

import io.swagger.model.user.UserIbanSearchDTO;
import io.swagger.model.user.UserSearchDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
@NamedNativeQuery(
        name = "findUserByIban",
        query =
                "SELECT USER.USER_ID AS user_id, USER.EMAIL AS email, USER.FIRSTNAME AS firstname, USER.LASTNAME AS lastname, ACCOUNT.ACCOUNT_ID AS iban FROM USER INNER JOIN ACCOUNT ON USER.USER_ID = ACCOUNT.USER_ID WHERE ACCOUNT.ACCOUNT_TYPE = 0 AND ACCOUNT.ACCOUNT_ID = :iban",
        resultSetMapping = "user_iban_search_dto"
)
@SqlResultSetMapping(
        name = "user_iban_search_dto",
        classes = @ConstructorResult(
                targetClass = UserIbanSearchDTO.class,
                columns = {
                        @ColumnResult(name = "user_id", type = UUID.class),
                        @ColumnResult(name = "firstname", type = String.class),
                        @ColumnResult(name = "lastname", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "iban", type = String.class)
                }
        )
)

// find all users with account

@NamedNativeQuery(
        name = "findUsersWithAccount",
        query =
                "SELECT USER.USER_ID AS user_id, USER.EMAIL AS email, USER.FIRSTNAME AS firstname, USER.LASTNAME AS lastname, ACCOUNT.ACCOUNT_ID AS iban FROM USER INNER JOIN ACCOUNT ON USER.USER_ID = ACCOUNT.USER_ID WHERE LOWER( USER.FIRSTNAME ) LIKE '%' || lower(:firstname) || '%' AND LOWER ( USER.LASTNAME ) LIKE '%' || lower(:lastname) || '%' AND ACCOUNT.ACCOUNT_TYPE != 1 AND ACCOUNT.ACCOUNT_TYPE != 2",
        resultSetMapping = "users_withaccount_dto"
)
@SqlResultSetMapping(
        name = "users_withaccount_dto",
        classes = @ConstructorResult(
                targetClass = UserIbanSearchDTO.class,
                columns = {
                        @ColumnResult(name = "user_id", type = UUID.class),
                        @ColumnResult(name = "firstname", type = String.class),
                        @ColumnResult(name = "lastname", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "iban", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "findUsersWithAccount.count",
        query =
                "SELECT count(*) as cnt FROM USER INNER JOIN ACCOUNT ON USER.USER_ID = ACCOUNT.USER_ID WHERE LOWER( USER.FIRSTNAME ) LIKE '%' || lower(:firstname) || '%' AND LOWER ( USER.LASTNAME ) LIKE '%' || lower(:lastname) || '%' AND ACCOUNT.ACCOUNT_TYPE != 1 AND ACCOUNT.ACCOUNT_TYPE != 2",
        resultSetMapping = "users_withaccount_dto.count"
)
@SqlResultSetMapping(name = "users_withaccount_dto.count", columns = @ColumnResult(name = "cnt"))

// find all users no account

@NamedNativeQuery(
        name = "findUsersWithNoAccount",
        query =
                "SELECT USER.USER_ID AS user_id, USER.EMAIL AS email, USER.FIRSTNAME AS firstname, USER.LASTNAME AS lastname FROM USER WHERE USER.USER_ID NOT IN (SELECT USER_ID FROM ACCOUNT) AND LOWER( USER.FIRSTNAME ) LIKE '%' || lower(:firstname) || '%' AND LOWER ( USER.LASTNAME ) LIKE '%' || lower(:lastname) || '%' ",
        resultSetMapping = "users_noaccount_dto"
)
@SqlResultSetMapping(
        name = "users_noaccount_dto",
        classes = @ConstructorResult(
                targetClass = UserSearchDTO.class,
                columns = {
                        @ColumnResult(name = "user_id", type = UUID.class),
                        @ColumnResult(name = "firstname", type = String.class),
                        @ColumnResult(name = "lastname", type = String.class),
                        @ColumnResult(name = "email", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "findUsersWithNoAccount.count",
        query =
                "SELECT count(*) as cnt FROM USER WHERE USER.USER_ID NOT IN (SELECT USER_ID FROM ACCOUNT) AND LOWER( USER.FIRSTNAME ) LIKE '%' || lower(:firstname) || '%' AND LOWER ( USER.LASTNAME ) LIKE '%' || lower(:lastname) || '%' ",
        resultSetMapping = "users_noaccount_dto.count"
)
@SqlResultSetMapping(name = "users_noaccount_dto.count", columns = @ColumnResult(name = "cnt"))

// Find all users

@NamedNativeQuery(
        name = "findUsersAll",
        query =
                "SELECT USER.USER_ID AS user_id, USER.EMAIL AS email, USER.FIRSTNAME AS firstname, USER.LASTNAME AS lastname, ACCOUNT.ACCOUNT_ID AS iban FROM USER" + " LEFT OUTER JOIN ACCOUNT ON USER.USER_ID = ACCOUNT.USER_ID" + " WHERE LOWER( USER.FIRSTNAME ) LIKE '%' || lower(:firstname) || '%' AND LOWER ( USER.LASTNAME ) LIKE '%' || lower(:lastname) || '%' AND ACCOUNT.ACCOUNT_TYPE != 2 AND ACCOUNT.ACCOUNT_TYPE != 1 OR ACCOUNT.ACCOUNT_TYPE is NULL",
        resultSetMapping = "users_all_dto"
)
@SqlResultSetMapping(
        name = "users_all_dto",
        classes = @ConstructorResult(
                targetClass = UserIbanSearchDTO.class,
                columns = {
                        @ColumnResult(name = "user_id", type = UUID.class),
                        @ColumnResult(name = "firstname", type = String.class),
                        @ColumnResult(name = "lastname", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "iban", type = String.class)
                }
        )
)
@NamedNativeQuery(
        name = "findUsersAll.count",
        query =
                "SELECT count(*) as cnt FROM USER LEFT OUTER JOIN ACCOUNT ON USER.USER_ID = ACCOUNT.USER_ID WHERE LOWER( USER.FIRSTNAME ) LIKE '%' || lower(:firstname) || '%' AND LOWER ( USER.LASTNAME ) LIKE '%' || lower(:lastname) || '%' AND ACCOUNT.ACCOUNT_TYPE != 2 AND ACCOUNT.ACCOUNT_TYPE != 1 OR ACCOUNT.ACCOUNT_TYPE is NULL",
        resultSetMapping = "users_all_dto.count"
)
@SqlResultSetMapping(name = "users_all_dto.count", columns = @ColumnResult(name = "cnt"))

public class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    private UUID user_id;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    @NonNull
    @Column(unique = true)
    private String email;

    @NonNull
    private BigDecimal transactionLimit;

    @NonNull
    private BigDecimal dailyLimit;

    @NonNull
    private String password;

    @Override
    public int hashCode()
    {
        return user_id.hashCode() * firstname.hashCode() * lastname.hashCode();
    }

    public void setRolesForUser(List<Role> roles)
    {
        Set<Role> newRoles = new HashSet<>(roles);
        this.setRoles(newRoles);
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User(String firstname, String lastname, String email, BigDecimal transactionLimit, BigDecimal dailyLimit, String password)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.transactionLimit = transactionLimit;
        this.dailyLimit = dailyLimit;
        this.password = password;
    }

    public User()
    {
    }
}
