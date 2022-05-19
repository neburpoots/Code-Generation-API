package io.swagger.repository;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

//    @Query("SELECT a FROM Account a WHERE a.user")
    List<Account> findByUser(User user);

}
