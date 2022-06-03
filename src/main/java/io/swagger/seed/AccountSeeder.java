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

        Account bankaccount = new Account(new BigDecimal(1000000), new BigDecimal(0), AccountType.BANK, true);
        bankaccount.setAccount_id("NL01INHO0000000001");
        bankaccount.setUser(users.get(0));


        Account account1 = new Account(new BigDecimal(500), new BigDecimal(-500), AccountType.PRIMARY, true);
        Account account2 = new Account(new BigDecimal(500), new BigDecimal(-500), AccountType.SAVINGS, true);
        Account account3 = new Account(new BigDecimal(1000), new BigDecimal(-200), AccountType.PRIMARY, true);
        Account account4 = new Account(new BigDecimal(5000), new BigDecimal(-100), AccountType.SAVINGS, false);
        Account account5 = new Account(new BigDecimal(3000), new BigDecimal(-200), AccountType.PRIMARY, true);
        Account account6 = new Account(new BigDecimal(4000), new BigDecimal(-100), AccountType.SAVINGS, true);
        Account account7 = new Account(new BigDecimal(2000), new BigDecimal(-100), AccountType.PRIMARY, true);
        Account account8 = new Account(new BigDecimal(2000), new BigDecimal(-150), AccountType.SAVINGS, true);

        account1.setUser(users.get(0));
        account2.setUser(users.get(0));
        account3.setUser(users.get(1));
        account4.setUser(users.get(1));
        account5.setUser(users.get(2));
        account6.setUser(users.get(2));
        account7.setUser(users.get(3));
        account8.setUser(users.get(3));

        return this.accountRepo.saveAll(
                List.of(bankaccount, account1, account2, account3, account4, account5, account6, account7, account8)
        );
    }
}