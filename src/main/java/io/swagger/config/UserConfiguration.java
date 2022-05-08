package io.swagger.config;

import io.swagger.model.entity.Role;
import io.swagger.model.entity.User;
import io.swagger.repository.RoleRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@Order(2)
public class UserConfiguration {

    private final RoleRepository roleRepo;

    @Autowired
    public UserConfiguration(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Bean
    CommandLineRunner userCommandLineRunner(UserRepository repository) {
        return args -> {

            User ruben = new User("Ruben", "Stoop", "670240@student.inholland.nl", 50, new BigDecimal(25000), "Welkom!");

            User tim = new User("Tim", "Roffelsen", "123456@student.inholland.nl", 50, new BigDecimal(25000), "Welkom!");

//            Gets all the roles for the users
            List<Role> roles = (List<Role>)this.roleRepo.findAll();
            ruben.setRolesForUser(roles);

            repository.saveAll(
                    List.of(ruben, tim)
            );
        };
    }
}
