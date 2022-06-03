package io.swagger.seed;

import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserSeeder {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final AccountRepository accountRepo;

    @Autowired
    public UserSeeder(UserRepository userRepo, RoleRepository roleRepo, AccountRepository accountRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.accountRepo = accountRepo;
    }

    @Autowired
    private WebSecurityConfig webSecurityConfig;

    public List<User> seed() {
        User bank = new User("Bank", "Account", "bankaccount@bankaccount.nl", new BigDecimal(1000000), new BigDecimal(1000000), webSecurityConfig.passwordEncoder().encode("Secret123!"));
        User ruben = new User("Ruben", "Stoop", "ruben@student.inholland.nl", new BigDecimal(500), new BigDecimal(2500), webSecurityConfig.passwordEncoder().encode("Secret123!"));
        User tim = new User("Tim", "Roffelsen", "tim@student.inholland.nl", new BigDecimal(500), new BigDecimal(2500), webSecurityConfig.passwordEncoder().encode("Secret123!"));
        User customer = new User("Mr", "Customer", "customer@student.inholland.nl", new BigDecimal(500), new BigDecimal(2500), webSecurityConfig.passwordEncoder().encode("Secret123!"));
        User noAccount = new User("No", "Account", "noaccount@student.inholland.nl", new BigDecimal(500), new BigDecimal(5000), webSecurityConfig.passwordEncoder().encode("Secret123!"));

        // Sets all the roles for the employee users
        ruben.setRolesForUser(List.of(roleRepo.findById(1).orElse(null), roleRepo.findById(2).orElse(null)));
        tim.setRolesForUser(List.of(roleRepo.findById(1).orElse(null), roleRepo.findById(2).orElse(null)));

        // Sets the role for a normal customer
        List<Role> customerRole = new ArrayList<>();
        customerRole.add(roleRepo.findById(1).orElse(null));
        customer.setRolesForUser(customerRole);

        return this.userRepo.saveAll(
                List.of(bank, ruben, tim, customer, noAccount)
        );



    }
}
