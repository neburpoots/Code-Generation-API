package io.swagger.repository;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

//    @Query("SELECT a FROM Account a WHERE a.user")
    List<Account> findByUser(User user);

    @Query("SELECT a.user FROM Account a WHERE a.account_id = :account_id")
    User findWithUser(@Param("account_id") String account_id);

    Page<Account> findByUserAndAccountType(User user, AccountType accountType, Pageable pageable);

    Page<Account> findByUserAndAccountTypeIsNot(User user, AccountType accountType, Pageable pageable);

    Page<Account> findByAccountType(AccountType accountType, Pageable pageable);

    Page<Account> findByAccountTypeIsNot(AccountType accountType, Pageable pageable);

//    @Query("select u from Account b where UPPER(b.firstName) like CONCAT('%',UPPER(?1),'%') and UPPER(b.lastName) like CONCAT('%',UPPER(?2),'%')";)
//    List<Customer> findByFirstNameLikeAndLastNameLike(String firstNameFilter, String lastNameFilter);
}
