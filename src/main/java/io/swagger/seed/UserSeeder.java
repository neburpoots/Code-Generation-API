package io.swagger.seed;

import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    public List<User> seed(List<Role> roles) {
        User ruben = new User("Ruben", "Stoop", "670240@student.inholland.nl", new BigDecimal(50), new BigDecimal(25000), webSecurityConfig.passwordEncoder().encode("Secret123!"));
        User tim = new User("Tim", "Roffelsen", "123456@student.inholland.nl", new BigDecimal(50), new BigDecimal(25000), webSecurityConfig.passwordEncoder().encode("Secret123!"));
        User test = new User("test", "test", "test@student.inholland.nl", new BigDecimal(50), new BigDecimal(25000), webSecurityConfig.passwordEncoder().encode("Secret123!"));

        // Sets all the roles for the employee users
        ruben.setRolesForUser(roles);
        tim.setRolesForUser(roles);

        // Sets the role for a normal customer
        List<Role> customerRole = roles;
        customerRole.remove(1);
        test.setRolesForUser(customerRole);

        return (List<User>)this.userRepo.saveAll(
                List.of(ruben, tim, test)
        );



    }
}
