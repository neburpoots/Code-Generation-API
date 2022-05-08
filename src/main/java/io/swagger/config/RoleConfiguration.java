package io.swagger.config;

import io.swagger.model.entity.Role;
import io.swagger.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@Order(1)
public class RoleConfiguration {

    @Bean
    CommandLineRunner roleCommandLineRunner(RoleRepository repository) {
        return args -> {
            Role customer = new Role("Customer");
            Role employee = new Role("Employee");

            repository.saveAll(
                    List.of(customer, employee)
            );
        };
    }
}
