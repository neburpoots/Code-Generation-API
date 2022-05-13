package io.swagger.seed;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.AccountType;
import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountSeeder {

    private final UserRepository userRepo;
    private final AccountRepository accountRepo;

    @Autowired
    public AccountSeeder(UserRepository userRepo, AccountRepository accountRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
    }

    public List<Account> seed(List<User> users) {
        Account account1 = new Account(new BigDecimal(500), new BigDecimal(500), AccountType.PRIMARY, true);
        Account account2 = new Account(new BigDecimal(500), new BigDecimal(500), AccountType.SAVINGS, true);


        account1.setUser(users.get(0));
        account2.setUser(users.get(1));

        return this.accountRepo.saveAll(
                List.of(account1, account2)
        );
    }
}